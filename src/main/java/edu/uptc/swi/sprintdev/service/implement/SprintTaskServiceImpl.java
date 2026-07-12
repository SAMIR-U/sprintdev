package edu.uptc.swi.sprintdev.service.implement;

import edu.uptc.swi.sprintdev.domain.*;
import edu.uptc.swi.sprintdev.exceptions.SprintIsClosedException;
import edu.uptc.swi.sprintdev.exceptions.StatusTaskIsNotPossibleToChangeException;
import edu.uptc.swi.sprintdev.exceptions.TheListNeedAtleastOneTaskException;
import edu.uptc.swi.sprintdev.exceptions.UserDontHavePermissionException;
import edu.uptc.swi.sprintdev.repository.ISprintTaskRepo;
import edu.uptc.swi.sprintdev.service.interfaces.ISprintTaskService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Set;


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
            task.setStatus(TaskStatus.PENDING);
            task.setCreationDate(LocalDateTime.now());
            sprint.setVersion(sprint.getVersion()+1);
            this.sprintTaskRepo.save(task);
            return true;
        }
        return false;
    }

    @Override
    public boolean updateTask(Task task, int creatorId) throws UserDontHavePermissionException {
        if (this.hasPermission(task.getSprint(), creatorId) && this.existsTask(task)) {
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
            return true;
        }
        throw new UserDontHavePermissionException("No cuenta con los permisos requeridos para esta acción");
    }

    @Override
    public Task findTaskById(int id) {
        return this.sprintTaskRepo.getReferenceById(id);
    }

    @Override
    public Set<User> findAssignedUserTask(Task task, int userId) throws UserDontHavePermissionException {
        if (this.hasPermission(task.getSprint(), userId)) {
            return task.getAssignedUsers();
        }
        throw new UserDontHavePermissionException("No cuenta con los permisos requeridos para esta acción");
    }

    @Override
    public boolean updateTaskStatus(Task task, int creatorId)
            throws UserDontHavePermissionException, StatusTaskIsNotPossibleToChangeException {
        Task existingTask = this.findTaskById(task.getId());
        this.validatePermission(existingTask, creatorId);
        this.validateStatusTransition(existingTask.getStatus(), task.getStatus());
        existingTask.setStatus(task.getStatus());
        this.sprintTaskRepo.save(existingTask);
        return true;
    }


    private boolean existsTask(Task task) {
        return this.sprintTaskRepo.existsById(task.getId());
    }

    private boolean hasPermission(Sprint sprint, int creatorId) {
        User creator = sprint.getCreator();
        return creator != null && creator.getId() == creatorId;
    }

    private int taskListSize(Sprint sprint) {
        return sprint.getTasks().size();
    }

    private void validatePermission(Task existingTask, int creatorId) throws UserDontHavePermissionException {
        if (!this.hasPermission(existingTask.getSprint(), creatorId)) {
            throw new UserDontHavePermissionException("No cuenta con los permisos requeridos para esta acción");
        }
    }

    private void validateStatusTransition(TaskStatus previous, TaskStatus next) throws StatusTaskIsNotPossibleToChangeException {
        if (!previous.mayAllowedTransition(next)) {
            throw new StatusTaskIsNotPossibleToChangeException("El cambio de estado no es posible: " + previous + " -> " + next);
        }
    }

}
