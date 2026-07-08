package edu.uptc.swi.sprintdev.repository;

import edu.uptc.swi.sprintdev.domain.Sprint;
import edu.uptc.swi.sprintdev.domain.Task;
import edu.uptc.swi.sprintdev.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ISprintRepo extends JpaRepository<Sprint, Integer> {
    @Query("SELECT DISTINCT s FROM Sprint s LEFT JOIN s.readers r " +
            "WHERE s.creator.id = :userId OR r.userId = :userId")
    List<Sprint> findAllUserSprints(@Param("userId") int userId);


    @Query("SELECT r FROM Sprint s JOIN s.readers r WHERE s.id = :sprintId")
    List<User> findSprintReaders(@Param("sprintId") int sprintId);

    @Query ("SELECT t FROM Sprint s JOIN s.tasks t WHERE s.sprintId =: sprintId")
    List<Task> findAllSprintTask(int sprintId);
}
