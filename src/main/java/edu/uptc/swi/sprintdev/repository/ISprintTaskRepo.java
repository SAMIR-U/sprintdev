package edu.uptc.swi.sprintdev.repository;

import edu.uptc.swi.sprintdev.domain.Task;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ISprintTaskRepo extends JpaRepository<Task, Integer> {
    //por defecto ya tiene create -> por medio de este puedo hacer el update y ya cuenta con el DeleteById
}
