package edu.uptc.swi.sprintdev.service.implement;

import edu.uptc.swi.sprintdev.domain.*;
import edu.uptc.swi.sprintdev.repository.ISprintRepo;
import edu.uptc.swi.sprintdev.service.interfaces.ISprintService;
import org.springframework.stereotype.Service;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class SprintServiceImpl implements ISprintService {
    private final ISprintRepo sprintRepo;

    public SprintServiceImpl(ISprintRepo sprintRepo) {
        this.sprintRepo = sprintRepo;
    }

    @Override
    public boolean createSprint(Sprint sprint) {
        sprint.setStatus(SprintStatus.CREATED);
        this.sprintRepo.save(sprint);
        return true;
    }

    @Override
    public List<Sprint> obtainMySprints(int userId) {
        List<Sprint> sprints = this.sprintRepo.findAllUserSprints(userId);

        return sprints.stream()
                .sorted(Comparator.comparing(Sprint::getStartDate).reversed())
                .collect(Collectors.toList());
    }

    @Override
    public List<User> findAllReadersSprint(int sprintId, int userId) {
        Sprint sprint = this.findSprintById(sprintId);
        if (this.hasAccess(sprint, userId)) {
            return this.sprintRepo.findSprintReaders(sprintId);
        }
        return null;
    }

    @Override
    public boolean closeSprint(int sprintId, int creatorId) {
        Sprint sprint = this.sprintRepo.getReferenceById(sprintId);
        if (this.validateCloseSprintConditions(sprint, creatorId)) {
            sprint.setStatus(SprintStatus.CLOSED);
            return true;
        }
        return false;
    }

    @Override
    public boolean activateSprint(int sprintId, int creatorId) {
        Sprint sprint = this.findSprintById(sprintId, creatorId);
        if (this.validateActivateSprintConditions(sprint, creatorId)) {
            sprint.setStatus(SprintStatus.ACTIVE);
            return true;
        }
        return false;
    }

    @Override
    public boolean addReaderToSprint(int sprintId, int creatorId, User user) {
        Sprint sprint = findSprintById(sprintId, creatorId);
        if (sprint == null) {
            return false;
        }
        if (!validateAddReaderConditions(sprint, user.getId())) {
            return false;
        }
        sprint.getReaders().add(user);
        return true;
    }

    @Override
    public Sprint findSprintById(int sprintId, int userId) {
        Sprint sprint = this.findSprintById(sprintId);
        if (this.hasAccess(sprint, userId)) {
            return sprint;
        }
            return null;
    }

    private boolean validateAddReaderConditions(Sprint sprint, int userId) {
        return this.validateReaderListSize(sprint) && !this.isReader( sprint, userId);
    }

    private boolean validateReaderListSize(Sprint sprint) {
        return sprint.getReaders().size() < 8;
    }


    private int obtainTaskListSize(int sprintId) {
        return this.sprintRepo.findAllSprintTask(sprintId).size();
    }

    private boolean validateActivateSprintConditions(Sprint sprint, int creatorId) {
        return this.obtainTaskListSize(sprint.getSprintId()) > 0 && sprint.getStatus() == SprintStatus.ACTIVE && this.isCreator(sprint, creatorId);
    }

    private boolean validateCloseSprintConditions(Sprint sprint, int creatorId) {
        if (this.isCreator(sprint, creatorId)) {
            if (sprint.getStatus() == SprintStatus.ACTIVE) {
                for (Task task : sprint.getTasks()) {
                    if (task.getStatus() != TaskStatus.COMPLETED) {
                        return false;
                    }
                }
                return true;
            }
        }

        return false;
    }
    private boolean isReader(Sprint sprint, int userId) {
        for (User user : sprint.getReaders()) {
            if (user.getId() == userId) {
                return true;
            }
        }
        return false;
    }
    private boolean isCreator(Sprint sprint, int userId) {
        User creator = sprint.getCreator();
        return creator.getId() == userId;
    }

    private boolean hasAccess(Sprint sprint, int userId) {
        return this.isCreator(sprint, userId) || this.isReader(sprint, userId);
    }

    private Sprint findSprintById(int sprintId) {
        return this.sprintRepo.getReferenceById(sprintId);
    }
}
