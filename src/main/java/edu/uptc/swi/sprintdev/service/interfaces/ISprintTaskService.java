package edu.uptc.swi.sprintdev.service.interfaces;

import edu.uptc.swi.sprintdev.domain.Task;
import edu.uptc.swi.sprintdev.exceptions.*;



/**
 * Service interface for task operations inside a sprint.
 * Defines task creation, update, deletion, retrieval and status operations
 * with permission and sprint state validation.
 */
public interface ISprintTaskService {
    /**
     * Create a new task in a sprint.
     *
     * @param task the task to create
     * @param creatorId the identifier of the user creating the task
     * @return true if task creation succeeds
     * @throws UserDontHavePermissionException when the user lacks permission
     * @throws SprintIsClosedException when the sprint is already closed
     */
    boolean createTask(Task task, int creatorId) throws UserDontHavePermissionException, SprintIsClosedException;

    /**
     * Update an existing task.
     *
     * @param task the task with updated values
     * @param creatorId the identifier of the user performing the update
     * @return true if the task was updated successfully
     * @throws UserDontHavePermissionException when the user lacks permission
     */
    boolean updateTask(Task task, int creatorId) throws UserDontHavePermissionException;

    /**
     * Delete a task from a sprint.
     *
     * @param task the task to remove
     * @param creatorId the identifier of the user requesting deletion
     * @return true if the task was deleted successfully
     * @throws UserDontHavePermissionException when the user lacks permission
     * @throws TheListNeedAtleastOneTaskException when deletion would leave no tasks
     */
    boolean deleteTask(Task task, int creatorId) throws UserDontHavePermissionException, TheListNeedAtleastOneTaskException;

    /**
     * Find a task by its identifier.
     *
     * @param id the task identifier
     * @return the task entity
     * @throws UserDontHavePermissionException when the current user lacks permission
     */
    Task findTaskById(int id) throws UserDontHavePermissionException;
    /**
     * Update the status of a task.
     *
     * @param task the task whose status will change
     * @param creatorId the identifier of the user requesting the status update
     * @return true if the task status was updated successfully
     * @throws UserDontHavePermissionException when the user lacks permission
     * @throws StatusTaskIsNotPossibleToChangeException when the requested status transition is invalid
     * @throws SprintIsNotActiveException when the sprint is not active
     */
    boolean updateTaskStatus(Task task, int creatorId) throws UserDontHavePermissionException, StatusTaskIsNotPossibleToChangeException, SprintIsNotActiveException;
}
