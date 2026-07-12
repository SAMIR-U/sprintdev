package edu.uptc.swi.sprintdev.service.interfaces;

import edu.uptc.swi.sprintdev.domain.Task;
import edu.uptc.swi.sprintdev.domain.User;
import edu.uptc.swi.sprintdev.exceptions.*;

import java.util.Set;


public interface ISprintTaskService {
    boolean createTask(Task task, int creatorId) throws UserDontHavePermissionException, SprintIsClosedException;
    boolean updateTask(Task task, int creatorId) throws UserDontHavePermissionException;
    boolean deleteTask(Task task, int creatorId) throws UserDontHavePermissionException, TheListNeedAtleastOneTaskException;
    Task findTaskById(int id) throws UserDontHavePermissionException;
    Set<User> findAssignedUserTask(Task task, int userId)throws UserDontHavePermissionException;
    boolean updateTaskStatus(Task task, int creatorId) throws UserDontHavePermissionException, StatusTaskIsNotPossibleToChangeException, SprintIsNotActiveException;
}
