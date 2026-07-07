package edu.uptc.swi.sprintdev.repository;

import edu.uptc.swi.sprintdev.domain.Sprint;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ISprintRepo extends JpaRepository<Sprint, Integer> {
    @Query("SELECT DISTINCT s FROM Sprint s LEFT JOIN s.readers r " +
            "WHERE s.creator.id = :userId OR r.id = :userId")
    List<Sprint> findAllUserSprints(@Param("userId") int userId);

}
