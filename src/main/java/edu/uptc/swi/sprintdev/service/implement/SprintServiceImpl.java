package edu.uptc.swi.sprintdev.service.implement;

import edu.uptc.swi.sprintdev.domain.Sprint;
import edu.uptc.swi.sprintdev.domain.SprintStatus;
import edu.uptc.swi.sprintdev.domain.User;
import edu.uptc.swi.sprintdev.repository.ISprintRepo;
import edu.uptc.swi.sprintdev.service.interfaces.ISprintService;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
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
    public List<User> findAllReadersSprint(int sprintId, int page) {
        Pageable pageable = PageRequest.of(page, 5);
        return this.sprintRepo.findSprintReaders(sprintId, pageable);
    }


}
