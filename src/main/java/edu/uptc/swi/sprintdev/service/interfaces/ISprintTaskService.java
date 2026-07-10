package edu.uptc.swi.sprintdev.service.interfaces;

import edu.uptc.swi.sprintdev.domain.Task;
import edu.uptc.swi.sprintdev.domain.User;
import edu.uptc.swi.sprintdev.exceptions.UserDontHavePermissionException;

import java.util.List;


public interface ISprintTaskService {
    boolean createTask(Task task, int creatorId) throws UserDontHavePermissionException;
    boolean updateTask(Task task, int creatorId) throws UserDontHavePermissionException;
    boolean deleteTask(Task task, int creatorId) throws UserDontHavePermissionException;
    Task findTaskById(int id) throws UserDontHavePermissionException;
    List<User> findAssignedUserTask(Task task, int userId)throws UserDontHavePermissionException;
}
