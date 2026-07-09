package edu.uptc.swi.sprintdev.service.interfaces;

import edu.uptc.swi.sprintdev.domain.Task;

public interface ISprintTaskService {
    boolean createTask(Task task, int creatorId);
    boolean updateTask(Task task, int creatorId);
    boolean deleteTask(Task task, int creatorId);
    Task findTaskById(int id);
}
