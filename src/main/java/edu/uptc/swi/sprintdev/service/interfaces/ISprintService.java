package edu.uptc.swi.sprintdev.service.interfaces;

import edu.uptc.swi.sprintdev.domain.Sprint;
import edu.uptc.swi.sprintdev.domain.User;
import edu.uptc.swi.sprintdev.exceptions.UserDontHavePermissionException;

import java.util.List;

public interface ISprintService {
    boolean createSprint(Sprint sprint);
    List<Sprint> obtainMySprints(int userId);
    List<User> findAllReadersSprint(int sprintId, int userId) throws UserDontHavePermissionException;
    boolean closeSprint(int sprintId, int creatorId) throws UserDontHavePermissionException;
    boolean activateSprint(int sprintId, int creatorId) throws UserDontHavePermissionException;
    boolean addReaderToSprint(int sprintId,int creatorId ,User reader) throws UserDontHavePermissionException;
    Sprint findSprintById(int sprintId, int userId) throws UserDontHavePermissionException;
    User obtainCreator(int sprintId);
}
