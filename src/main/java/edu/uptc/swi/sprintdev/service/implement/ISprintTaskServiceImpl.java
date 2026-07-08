package edu.uptc.swi.sprintdev.service.implement;

import edu.uptc.swi.sprintdev.domain.Task;
import edu.uptc.swi.sprintdev.repository.ISprintTaskRepo;
import edu.uptc.swi.sprintdev.service.interfaces.ISprintTaskService;
import org.springframework.stereotype.Service;

@Service
public class ISprintTaskServiceImpl implements ISprintTaskService {
    private final ISprintTaskRepo sprintTaskRepo;

    public ISprintTaskServiceImpl(ISprintTaskRepo sprintTaskRepo) {
        this.sprintTaskRepo = sprintTaskRepo;
    }

    @Override
    public boolean createTask(Task task) {
        if (!this.existsTask(task)) {
            this.sprintTaskRepo.save(task);
            return true;
        }
        return false;
    }

    @Override
    public boolean updateTask(Task task) {
        if (this.existsTask(task)) {
            this.sprintTaskRepo.save(task);
            return true;
        }
        return false;
    }

    @Override
    public boolean deleteTask(Task task) {
        if (this.existsTask(task)) {
            this.sprintTaskRepo.deleteById(task.getId());
            return true;
        }
        return false;
    }

    @Override
    public Task findTaskById(int id) {
        return this.sprintTaskRepo.getReferenceById(id);
    }

    private boolean existsTask(Task task) {
        return this.sprintTaskRepo.existsById(task.getId());
    }

}
