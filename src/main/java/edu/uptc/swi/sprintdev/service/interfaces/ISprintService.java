package edu.uptc.swi.sprintdev.service.interfaces;

import edu.uptc.swi.sprintdev.domain.Sprint;

import java.util.List;

public interface ISprintService {
    boolean createSprint(Sprint sprint);
    List<Sprint> obtainMySprints(int userId);
}
