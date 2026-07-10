package edu.uptc.swi.sprintdev.service.implement;


import edu.uptc.swi.sprintdev.domain.*;
import edu.uptc.swi.sprintdev.exceptions.InvalidDateException;
import edu.uptc.swi.sprintdev.exceptions.UserAlreadyExistInListException;
import edu.uptc.swi.sprintdev.exceptions.TheListIsFullException;
import edu.uptc.swi.sprintdev.exceptions.UserDontHavePermissionException;
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
    public boolean createSprint(Sprint sprint) throws InvalidDateException {
        if (this.validateSprintDates(sprint)) {
            sprint.setStatus(SprintStatus.CREATED);
            this.sprintRepo.save(sprint);
            return true;
        }
        throw new InvalidDateException("La fecha de fin no puede ser anterior a la fecha de inicio");
    }

    @Override
    public List<Sprint> obtainMySprints(int userId) {
        List<Sprint> sprints = this.sprintRepo.findAllUserSprints(userId);

        return sprints.stream()
                .sorted(Comparator.comparing(Sprint::getStartDate).reversed())
                .collect(Collectors.toList());
    }

    @Override
    public List<User> findAllReadersSprint(int sprintId, int userId) throws UserDontHavePermissionException {
        Sprint sprint = this.findSprintById(sprintId);
        if (this.hasAccess(sprint, userId)) {
            return this.sprintRepo.findSprintReaders(sprintId);
        }
        throw new UserDontHavePermissionException("No cuenta con los permisos requeridos para esta acción");
    }

    @Override
    public boolean closeSprint(int sprintId, int creatorId) throws UserDontHavePermissionException {
        Sprint sprint = this.sprintRepo.getReferenceById(sprintId);
        if (this.validateCloseSprintConditions(sprint, creatorId)) {
            sprint.setStatus(SprintStatus.CLOSED);
            this.sprintRepo.save(sprint);
            return true;
        }
        return false;
    }

    @Override
    public boolean activateSprint(int sprintId, int creatorId) {
        Sprint sprint = this.findSprintById(sprintId, creatorId);
        if (this.validateActivateSprintConditions(sprint, creatorId)) {
            sprint.setStatus(SprintStatus.ACTIVE);
            this.sprintRepo.save(sprint);
            return true;
        }

        return false;
    }

    @Override
    public boolean addReaderToSprint(int sprintId, int creatorId, User reader) throws UserDontHavePermissionException, UserAlreadyExistInListException, TheListIsFullException {
        Sprint sprint = findSprintById(sprintId, creatorId);
        if (sprint == null) {
            return false;
        }
        if (!validateAddReaderConditions(sprint, reader.getId())) {
            return false;
        }
        if (this.isCreator(sprint, creatorId)) {
            sprint.getReaders().add(reader);
            this.sprintRepo.save(sprint);
            return true;
        }
        throw new UserDontHavePermissionException("El usuario no tiene los permisos requeridos para esta acción");
    }

    @Override
    public Sprint findSprintById(int sprintId, int userId) throws UserDontHavePermissionException {
        Sprint sprint = this.findSprintById(sprintId);
        if (this.hasAccess(sprint, userId)) {
            return sprint;
        }
        throw new UserDontHavePermissionException("No cuenta con los permisos requeridos para esta acción");
    }

    @Override
    public User obtainCreator(int sprintId) {
        Sprint sprint = this.findSprintById(sprintId);
        return sprint.getCreator();
    }

    @Override
    public List<Task> findAllSprintTasks(int sprintId, int userId) throws UserDontHavePermissionException {
        Sprint sprint = this.findSprintById(sprintId);
        if (this.hasAccess(sprint, userId)) {
            return sprint.getTasks();
        }
        throw new UserDontHavePermissionException("No cuenta con los permisos requeridos para realizar esta acción");
    }

    private boolean validateAddReaderConditions(Sprint sprint, int userId) throws UserAlreadyExistInListException, TheListIsFullException {
        if (!this.validateReaderListSize(sprint)) {
            throw new TheListIsFullException("La lista esta llena");
        }
        if (this.isReader(sprint, userId) || this.isCreator(sprint, userId)) {
            throw new UserAlreadyExistInListException("El usuario ya se encuentra en la lista");
        }
        return true;
    }

    private boolean validateReaderListSize(Sprint sprint) {
        return sprint.getReaders().size() < 8;
    }

    private boolean validateActivateSprintConditions(Sprint sprint, int creatorId) {
        return !sprint.getTasks().isEmpty()
                && sprint.getStatus() == SprintStatus.CREATED
                && isCreator(sprint, creatorId);
    }

    private boolean validateCloseSprintConditions(Sprint sprint, int creatorId) throws UserDontHavePermissionException {
        if (!isCreator(sprint, creatorId)) {
            throw new UserDontHavePermissionException("No cuenta con los permisos requeridos para esta acción");
        }
        if (sprint.getStatus() != SprintStatus.ACTIVE) {
            return false;
        }
        for (Task task : sprint.getTasks()) {
            if (task.getStatus() != TaskStatus.COMPLETED) {
                return false;
            }
        }

        return true;
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

    private boolean validateSprintDates(Sprint sprint) {
        return sprint.getStartDate().isBefore(sprint.getEndDate());
    }
}
