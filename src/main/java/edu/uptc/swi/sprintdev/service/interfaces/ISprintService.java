package edu.uptc.swi.sprintdev.service.interfaces;

import edu.uptc.swi.sprintdev.domain.Sprint;
import edu.uptc.swi.sprintdev.domain.User;

import java.util.List;

public interface ISprintService {
    boolean createSprint(Sprint sprint);
    List<Sprint> obtainMySprints(int userId);
    List<User> findAllReadersSprint(int sprintId);
    Boolean closeSprint(int sprintId, int userId);
    Boolean activateSprint(int sprintId, int userId);
    Boolean addReaderToSprint(int sprintId,int creatorId ,User user);
    Sprint findSprintById(int sprintId, int userId);
}
