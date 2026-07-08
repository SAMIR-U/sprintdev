package edu.uptc.swi.sprintdev.service.serviceInterface;

import edu.uptc.swi.sprintdev.domain.Task;

public interface ISprintTaskService {
    boolean createTask(Task task);
    boolean updateTask(Task task);
    boolean deleteTask(Task task);
    Task findTaskById(int id);
}
