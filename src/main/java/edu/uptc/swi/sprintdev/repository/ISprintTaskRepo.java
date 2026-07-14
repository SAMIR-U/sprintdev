package edu.uptc.swi.sprintdev.repository;

import edu.uptc.swi.sprintdev.domain.Task;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Repository interface for sprint task persistence operations.
 */
public interface ISprintTaskRepo extends JpaRepository<Task, Integer> {
}
