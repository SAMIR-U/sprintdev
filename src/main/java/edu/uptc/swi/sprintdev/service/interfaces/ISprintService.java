package edu.uptc.swi.sprintdev.service.interfaces;

import edu.uptc.swi.sprintdev.domain.Sprint;
import edu.uptc.swi.sprintdev.domain.User;

import java.util.List;

public interface ISprintService {
    boolean createSprint(Sprint sprint);
    List<Sprint> obtainMySprints(int userId);
    List<User> findAllReadersSprint(int sprintId, int page);
}
