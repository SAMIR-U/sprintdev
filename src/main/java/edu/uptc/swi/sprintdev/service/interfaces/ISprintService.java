package edu.uptc.swi.sprintdev.service.interfaces;

import edu.uptc.swi.sprintdev.domain.Sprint;
import edu.uptc.swi.sprintdev.domain.Task;
import edu.uptc.swi.sprintdev.domain.User;
import edu.uptc.swi.sprintdev.exceptions.InvalidDateException;
import edu.uptc.swi.sprintdev.exceptions.TheListIsFullException;
import edu.uptc.swi.sprintdev.exceptions.UserAlreadyExistInListException;
import edu.uptc.swi.sprintdev.exceptions.UserDontHavePermissionException;

import java.util.List;

/**
 * Service contract for sprint management operations.
 *
 * Provides methods to create, retrieve, activate, close, and manage readers
 * for sprints, while enforcing permission and validation rules.
 */
public interface ISprintService {
    /**
     * Create a new sprint.
     *
     * @param sprint the sprint entity to persist
     * @return true if the sprint was created successfully
     * @throws InvalidDateException when the sprint date range is invalid
     */
    boolean createSprint(Sprint sprint) throws InvalidDateException;

    /**
     * Retrieve a list of sprints associated with a specific user.
     *
     * @param userId the user identifier
     * @return list of sprints the user is related to
     */
    List<Sprint> obtainMySprints(int userId);

    /**
     * Find all readers assigned to a given sprint.
     *
     * @param sprintId the sprint identifier
     * @param userId the requesting user's identifier
     * @return list of reader users for the sprint
     * @throws UserDontHavePermissionException when the requesting user lacks permission
     */
    List<User> findAllReadersSprint(int sprintId, int userId) throws UserDontHavePermissionException;

    /**
     * Close an active sprint.
     *
     * @param sprintId the sprint identifier
     * @param creatorId the identifier of the sprint creator requesting the action
     * @return true if the sprint was successfully closed
     * @throws UserDontHavePermissionException when the requesting user lacks permission
     */
    boolean closeSprint(int sprintId, int creatorId) throws UserDontHavePermissionException;

    /**
     * Activate a sprint that is currently inactive.
     *
     * @param sprintId the sprint identifier
     * @param creatorId the identifier of the sprint creator requesting activation
     * @return true if the sprint was successfully activated
     * @throws UserDontHavePermissionException when the requesting user lacks permission
     */
    boolean activateSprint(int sprintId, int creatorId) throws UserDontHavePermissionException;

    /**
     * Add a reader user to a sprint.
     *
     * @param sprintId the sprint identifier
     * @param creatorId the identifier of the sprint creator requesting the addition
     * @param reader the user to add as reader
     * @return true if the reader was added successfully
     * @throws UserDontHavePermissionException when the requesting user lacks permission
     * @throws UserAlreadyExistInListException when the reader is already assigned to the sprint
     * @throws TheListIsFullException when the sprint has reached its reader limit
     */
    boolean addReaderToSprint(int sprintId,int creatorId ,User reader) throws UserDontHavePermissionException, UserAlreadyExistInListException, TheListIsFullException;

    /**
     * Retrieve a sprint by its identifier if the user has access.
     *
     * @param sprintId the sprint identifier
     * @param userId the requesting user's identifier
     * @return the sprint entity
     * @throws UserDontHavePermissionException when the requesting user lacks permission
     */
    Sprint findSprintById(int sprintId, int userId) throws UserDontHavePermissionException;

    /**
     * Find all tasks belonging to a sprint for a user.
     *
     * @param sprintId the sprint identifier
     * @param userId the requesting user's identifier
     * @return list of tasks in the sprint
     * @throws UserDontHavePermissionException when the requesting user lacks permission
     */
    List<Task> findAllSprintTasks(int sprintId, int userId) throws UserDontHavePermissionException;

}
