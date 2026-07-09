package edu.uptc.swi.sprintdev.service.interfaces;

import edu.uptc.swi.sprintdev.domain.Task;
import edu.uptc.swi.sprintdev.exceptions.UserDontHavePermissionException;

public interface ISprintTaskService {
    boolean createTask(Task task, int creatorId) throws UserDontHavePermissionException;
    boolean updateTask(Task task, int creatorId) throws UserDontHavePermissionException;
    boolean deleteTask(Task task, int creatorId) throws UserDontHavePermissionException;
    Task findTaskById(int id) throws UserDontHavePermissionException;
}
