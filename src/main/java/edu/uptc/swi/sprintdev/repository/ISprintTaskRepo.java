package edu.uptc.swi.sprintdev.repository;

import edu.uptc.swi.sprintdev.domain.Task;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ISprintTaskRepo extends JpaRepository<Task, Integer> {
}
