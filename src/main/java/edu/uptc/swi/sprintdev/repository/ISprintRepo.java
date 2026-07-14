package edu.uptc.swi.sprintdev.repository;

import edu.uptc.swi.sprintdev.domain.Sprint;
import edu.uptc.swi.sprintdev.domain.Task;
import edu.uptc.swi.sprintdev.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

/**
 * Repository interface for Sprint persistence operations.
 * Provides custom queries to load sprints and related sprint data.
 */
public interface ISprintRepo extends JpaRepository<Sprint, Integer> {
    /**
     * Finds all sprints that the given user either created or can read.
     *
     * @param userId the id of the user whose sprints should be found
     * @return a list of sprints created by or shared with the user
     */
    @Query("SELECT DISTINCT s FROM Sprint s LEFT JOIN s.readers r " +
            "WHERE s.creator.id = :userId OR r.userId = :userId")
    List<Sprint> findAllUserSprints(@Param("userId") int userId);


    /**
     * Returns all users who are readers of a sprint.
     *
     * @param sprintId the id of the sprint whose readers should be retrieved
     * @return a list of users assigned as sprint readers
     */
    @Query("SELECT r FROM Sprint s JOIN s.readers r WHERE s.id = :sprintId")
    List<User> findSprintReaders(@Param("sprintId") int sprintId);

    /**
     * Returns all tasks that belong to a sprint.
     *
     * @param sprintId the id of the sprint whose tasks should be loaded
     * @return a list of tasks associated with the sprint
     */
    @Query ("SELECT t FROM Sprint s JOIN s.tasks t WHERE s.sprintId =: sprintId")
    List<Task> findAllSprintTask(int sprintId);
}

