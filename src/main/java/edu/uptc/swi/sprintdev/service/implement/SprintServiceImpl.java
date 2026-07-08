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

    @Override
    public List<User> findAllReadersSprint(int sprintId) {
        return this.sprintRepo.findSprintReaders(sprintId);
    }

    @Override
    public Boolean closeSprint(int sprintId, int userId) {
        Sprint sprint = this.sprintRepo.getReferenceById(sprintId);
        if (this.validateCloseSprintConditions(sprint)) {
            sprint.setStatus(SprintStatus.CLOSED);
            return true;
        }
        return false;
    }

    @Override
    public Boolean activateSprint(int sprintId, int userId) {
        Sprint sprint = this.findSprintById(sprintId);
        if (this.validateActivateSprintConditions(sprint)) {
            sprint.setStatus(SprintStatus.ACTIVE);
            return true;
        }
        return false;
    }

    @Override
    public Boolean addReaderToSprint(int sprintId, User user) {
        if (this.validateAddReaderConditions(sprintId, user)) {
            Sprint sprint = this.findSprintById(sprintId);
            sprint.getReaders().add(user);
            return true;
        }
        return false;
    }

    @Override
    public Sprint findSprintById(int sprintId, int userId) {
        return this.sprintRepo.getReferenceById(sprintId);
    }

    private boolean validateAddReaderConditions(int sprintId, User user) {
        return this.validateReaderListSize(sprintId) && !this.isReader(sprintId, user);
    }

    private boolean validateReaderListSize(int sprintId) {
        return this.findAllReadersSprint(sprintId).size() < 8;
    }

    private boolean isReader(int sprintId, User user) {
        return this.sprintRepo.findSprintReaders(sprintId).contains(user);
    }

    private int obtainTaskListSize(int sprintId) {
        return this.sprintRepo.findAllSprintTask(sprintId).size();
    }

    private boolean validateActivateSprintConditions(Sprint sprint) {
        return this.obtainTaskListSize(sprint.getSprintId()) > 0 && sprint.getStatus() == SprintStatus.ACTIVE;
    }

    private boolean validateCloseSprintConditions(Sprint sprint) {
        if (sprint.getStatus() == SprintStatus.ACTIVE) {
            for (Task task : sprint.getTasks()) {
                if (task.getStatus() != TaskStatus.COMPLETED) {
                    return false;
                }
            }
            return true;
        }
        return false;
    }
    private boolean isCreator(Sprint sprint, int userId) {
        User creator = sprint.getCreator();
        return creator.getId() == userId;
    }
    private boolean hasAccess(Sprint sprint, int userId) {

        return false;
    }
}
