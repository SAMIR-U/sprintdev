package edu.uptc.swi.sprintdev.service;

import edu.uptc.swi.sprintdev.domain.Sprint;
import edu.uptc.swi.sprintdev.repository.ISprintRepo;

import java.util.List;

public class SprintServiceImpl implements ISprintService {
    private final ISprintRepo sprintRepo;

    public SprintServiceImpl(ISprintRepo sprintRepo) {
        this.sprintRepo = sprintRepo;
    }

    @Override
    public void createSprint(Sprint sprint) {
        this.sprintRepo.save(sprint);
    }

    @Override
    public List<Sprint> obtainMySprints(int userId) {
      return this.sprintRepo.findAllUserSprints(userId);
    }
}
