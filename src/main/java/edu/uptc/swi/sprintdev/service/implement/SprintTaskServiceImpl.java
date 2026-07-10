package edu.uptc.swi.sprintdev.service.implement;

import edu.uptc.swi.sprintdev.domain.Sprint;
import edu.uptc.swi.sprintdev.domain.Task;
import edu.uptc.swi.sprintdev.domain.TaskStatus;
import edu.uptc.swi.sprintdev.domain.User;
import edu.uptc.swi.sprintdev.exceptions.UserDontHavePermissionException;
import edu.uptc.swi.sprintdev.repository.ISprintTaskRepo;
import edu.uptc.swi.sprintdev.service.interfaces.ISprintTaskService;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Set;


@Service
public class SprintTaskServiceImpl implements ISprintTaskService {
    private final ISprintTaskRepo sprintTaskRepo;

    public SprintTaskServiceImpl(ISprintTaskRepo sprintTaskRepo) {
        this.sprintTaskRepo = sprintTaskRepo;
    }

    @Override
    public boolean createTask(Task task, int creatorId) throws UserDontHavePermissionException{
        if (!this.hasPermission(task.getSprint(), creatorId) && !this.existsTask(task)) {
            throw new UserDontHavePermissionException("No cuenta con los permisos requeridos para esta acción");
        }
        if (!this.existsTask(task)) {
            task.setStatus(TaskStatus.PENDING);
            task.setCreationDate(LocalDateTime.now());
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
    public boolean deleteTask(Task task, int creatorId) throws UserDontHavePermissionException {
        if (this.hasPermission(task.getSprint(), creatorId) && this.existsTask(task)) {
            this.sprintTaskRepo.deleteById(task.getId());
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

    private boolean existsTask(Task task) {
        return this.sprintTaskRepo.existsById(task.getId());
    }

    private boolean hasPermission(Sprint sprint, int creatorId) {
        User creator = sprint.getCreator();
        return creator != null && creator.getId() == creatorId;
    }

}
