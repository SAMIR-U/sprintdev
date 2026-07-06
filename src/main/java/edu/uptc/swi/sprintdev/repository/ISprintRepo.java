package edu.uptc.swi.sprintdev.repository;

import edu.uptc.swi.sprintdev.domain.Sprint;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ISprintRepo {
    @Query("SELECT s FROM Sprints s JOIN s.creator c WHERE c.userId = :userId ")
    List<Sprint> findAllUserSprint(@Param("userId") int userId);
}
