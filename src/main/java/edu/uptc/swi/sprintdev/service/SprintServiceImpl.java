package edu.uptc.swi.sprintdev.service;

import edu.uptc.swi.sprintdev.domain.Sprint;

import java.util.List;

public class SprintServiceImpl implements ISprintService {
    @Override
    public boolean createSprint(Sprint sprint) {
        return false;
    }

    @Override
    public List<Sprint> obtainMySprints(int userId) {
        return List.of();
    }
}
