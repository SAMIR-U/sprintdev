package edu.uptc.swi.sprintdev.service;

import edu.uptc.swi.sprintdev.domain.Sprint;
import edu.uptc.swi.sprintdev.domain.SprintStatus;
import edu.uptc.swi.sprintdev.repository.ISprintRepo;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

public class SprintServiceImpl implements ISprintService {
    private final ISprintRepo sprintRepo;

    public SprintServiceImpl(ISprintRepo sprintRepo) {
        this.sprintRepo = sprintRepo;
    }

    @Override
    public boolean createSprint(Sprint sprint) {
        sprint.setStatus(SprintStatus.CREATED);
        this.sprintRepo.save(sprint);
        for (Sprint s : sprintRepo.findAll()) {
            if (s.getSprintId() == sprint.getSprintId()) {
                return true;
            }
        }
        return false;
    }

    @Override
    public List<Sprint> obtainMySprints(int userId) {
        List<Sprint> sprints = this.sprintRepo.findAllUserSprints(userId);

        return sprints.stream()
                .sorted(Comparator.comparing(Sprint::getStartDate).reversed())
                .collect(Collectors.toList());
    }


}
