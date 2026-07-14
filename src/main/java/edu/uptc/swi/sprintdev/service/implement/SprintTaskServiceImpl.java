package edu.uptc.swi.sprintdev.service.implement;

import edu.uptc.swi.sprintdev.domain.*;
import edu.uptc.swi.sprintdev.exceptions.*;
import edu.uptc.swi.sprintdev.repository.ISprintTaskRepo;
import edu.uptc.swi.sprintdev.service.interfaces.ISprintTaskService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;


/**
 * Implementation of sprint task business logic.
 * This service handles task creation, update, deletion, assignment checks,
 * status transitions, and sprint version tracking.
 */
@Service
@Transactional
public class SprintTaskServiceImpl implements ISprintTaskService {
    private final ISprintTaskRepo sprintTaskRepo;

    public SprintTaskServiceImpl(ISprintTaskRepo sprintTaskRepo) {
        this.sprintTaskRepo = sprintTaskRepo;
    }

    @Override
    public boolean createTask(Task task, int creatorId) throws UserDontHavePermissionException, SprintIsClosedException {
        Sprint sprint = task.getSprint();
        if (sprint.getStatus() == SprintStatus.CLOSED) {
            throw new SprintIsClosedException("No es posible añadir tareas a un sprint ya cerrado");
        }
        if (!this.hasPermission(task.getSprint(), creatorId) && !this.existsTask(task)) {
            throw new UserDontHavePermissionException("No cuenta con los permisos requeridos para esta acción");
        }
        if (!this.existsTask(task)) {
            this.configCreateTask(task, sprint);
            this.sprintTaskRepo.save(task);
            return true;
        }
        return false;
    }

    @Override
    public boolean updateTask(Task task, int creatorId) throws UserDontHavePermissionException {
        if (this.hasPermission(task.getSprint(), creatorId) && this.existsTask(task)) {
            this.updateSprintVersion(task.getSprint());
            this.sprintTaskRepo.save(task);
            return true;
        }
        throw new UserDontHavePermissionException("No cuenta con los permisos requeridos para esta acción");
    }


    @Override
    public boolean deleteTask(Task task, int creatorId) throws UserDontHavePermissionException, TheListNeedAtleastOneTaskException {
        Sprint sprint = task.getSprint();
        if (this.taskListSize(sprint) == 1 && sprint.getStatus() == SprintStatus.ACTIVE) {
            throw new TheListNeedAtleastOneTaskException("Los sprints activos necesitan de almenos una tarea");
        }
        if (this.hasPermission(sprint, creatorId) && this.existsTask(task)) {
            sprint.getTasks().removeIf(t -> t.getId() == task.getId());
            this.updateSprintVersion(sprint);
            return true;
        }
        throw new UserDontHavePermissionException("No cuenta con los permisos requeridos para esta acción");
    }

    @Override
    public Task findTaskById(int id) {
        return this.sprintTaskRepo.getReferenceById(id);
    }



    @Override
    public boolean updateTaskStatus(Task task, int creatorId)
            throws UserDontHavePermissionException, StatusTaskIsNotPossibleToChangeException, SprintIsNotActiveException {
        Task existingTask = this.findTaskById(task.getId());
        if ( this.validateSprintIsActive(existingTask.getSprint())) {
            this.validatePermission(existingTask, creatorId);
            this.validateStatusTransition(existingTask.getStatus(), task.getStatus());
            existingTask.setStatus(task.getStatus());
            this.updateSprintVersion(existingTask.getSprint());
            this.sprintTaskRepo.save(existingTask);
            return true;
        }
        return false;
    }

    private boolean existsTask(Task task) {
        return this.sprintTaskRepo.existsById(task.getId());
    }

    /**
     * Check whether the given user is the creator of the sprint.
     *
     * @param sprint the sprint to inspect
     * @param creatorId the user identifier to verify
     * @return true when the user is the sprint creator
     */
    private boolean hasPermission(Sprint sprint, int creatorId) {
        User creator = sprint.getCreator();
        return creator != null && creator.getId() == creatorId;
    }

    /**
     * Count the number of tasks currently assigned to the sprint.
     *
     * @param sprint the sprint to evaluate
     * @return the number of tasks in the sprint
     */
    private int taskListSize(Sprint sprint) {
        return sprint.getTasks().size();
    }

    /**
     * Ensure the operation is performed by the sprint creator.
     *
     * @param existingTask the task belonging to the sprint
     * @param creatorId the user requesting the operation
     * @throws UserDontHavePermissionException when the user is not the creator
     */
    private void validatePermission(Task existingTask, int creatorId) throws UserDontHavePermissionException {
        if (!this.hasPermission(existingTask.getSprint(), creatorId)) {
            throw new UserDontHavePermissionException("No cuenta con los permisos requeridos para esta acción");
        }
    }

    /**
     * Validate that a task status transition is allowed.
     *
     * @param previous the current task status
     * @param next the requested task status
     * @throws StatusTaskIsNotPossibleToChangeException when the transition is invalid
     */
    private void validateStatusTransition(TaskStatus previous, TaskStatus next) throws StatusTaskIsNotPossibleToChangeException {
        String previousStr = ("" + previous).toLowerCase();
        String nextStr = ("" + next).toLowerCase();
        if (!previous.mayAllowedTransition(next)) {
            throw new StatusTaskIsNotPossibleToChangeException("El cambio de estado no es posible: " + previousStr + " -> " + nextStr);
        }
    }

    /**
     * Configure default fields when creating a new task.
     *
     * @param task the task to configure
     * @param sprint the sprint that contains the task
     */
    private void configCreateTask(Task task, Sprint sprint) {
        task.setStatus(TaskStatus.PENDING);
        task.setCreationDate(LocalDateTime.now());
        this.updateSprintVersion(sprint);
    }

    /**
     * Increment the sprint version number when tasks change.
     *
     * @param sprint the sprint to update
     */
    private void updateSprintVersion(Sprint sprint){
        sprint.setVersion(sprint.getVersion()+1);
    }

    /**
     * Validate that the sprint is active before allowing status changes.
     *
     * @param sprint the sprint to check
     * @return true when the sprint is active
     * @throws SprintIsNotActiveException when the sprint is not active
     */
    private boolean validateSprintIsActive(Sprint sprint)throws SprintIsNotActiveException{
        if (sprint.getStatus() !=  SprintStatus.ACTIVE) {
            throw new SprintIsNotActiveException("El sprint debe estar activo para permitir el cambio de estado");
        }
        return true;
    }
}
