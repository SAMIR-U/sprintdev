package edu.uptc.swi.sprintdev.service;

import edu.uptc.swi.sprintdev.domain.Sprint;

import java.util.List;

public interface ISprintService {
    void createSprint(Sprint sprint);
    List<Sprint> obtainMySprints(int userId);
}
