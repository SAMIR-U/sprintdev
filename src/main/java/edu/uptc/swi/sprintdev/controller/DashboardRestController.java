package edu.uptc.swi.sprintdev.controller;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.web.bind.annotation.RestController;

import edu.uptc.swi.sprintdev.domain.Sprint;
import edu.uptc.swi.sprintdev.domain.Task;
import edu.uptc.swi.sprintdev.domain.TaskStatus;
import edu.uptc.swi.sprintdev.domain.User;
import edu.uptc.swi.sprintdev.exceptions.StatusTaskIsNotPossibleToChangeException;
import edu.uptc.swi.sprintdev.exceptions.UserDontHavePermissionException;
import edu.uptc.swi.sprintdev.service.interfaces.ISprintService;
import edu.uptc.swi.sprintdev.service.interfaces.ISprintTaskService;
import jakarta.persistence.EntityNotFoundException;
import jakarta.servlet.http.HttpSession;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.PostMapping;

@RestController
@RequestMapping("/api")
public class DashboardRestController extends AbstractController{
    private final ISprintService sprintService;
    private final ISprintTaskService sprintTaskService;

    public DashboardRestController(ISprintService sprintService, ISprintTaskService sprintTaskService){
        this.sprintService = sprintService;
        this.sprintTaskService = sprintTaskService;
    }

    @GetMapping("/sprint/version")
    @Transactional(readOnly = true)
    public ResponseEntity<SprintDashboardResponse> isLastVersion(@RequestParam int sprintId,
                                                                 @RequestParam int version,
                                                                 HttpSession session) {
        User user = autenticatedUserIn(session);
        if (user==null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        try {
            Sprint sprint = sprintService.findSprintById(sprintId, user.getId());
            if (equalSprintVersion(sprint.getVersion(), version)) {
                return ResponseEntity.noContent().build();
            }
            return ResponseEntity.ok(SprintDashboardResponse.from(sprint));
        } catch (EntityNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        } catch(UserDontHavePermissionException e){
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }
    }

    @PostMapping("task/editstatus")
    public ResponseEntity<?> editStatus(@RequestParam int taskId,
                                        @RequestParam TaskStatus taskStatus,
                                        HttpSession session) {
        User user = autenticatedUserIn(session);
        if (user==null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
        Task task = new Task();
        task.setId(taskId);
        task.setStatus(taskStatus);
        try {
            boolean result = sprintTaskService.updateTaskStatus(task, user.getId());
            return ResponseEntity.ok(result);
        } catch (EntityNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("La tarea no existe.");
        } catch(UserDontHavePermissionException e){
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(e.getMessage());
        } catch (StatusTaskIsNotPossibleToChangeException e) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body(e.getMessage());
        }
    }

    private boolean equalSprintVersion(int sprint, int version){
        return sprint>0&&sprint==version;
    }

    private record SprintDashboardResponse(int version, List<TaskDashboardResponse> tasks) {
        private static SprintDashboardResponse from(Sprint sprint) {
            return new SprintDashboardResponse(sprint.getVersion(), sprint.getTasks().stream()
                    .map(TaskDashboardResponse::from).collect(Collectors.toList()));
        }
    }

    private record TaskDashboardResponse(int id, String title, TaskStatus status, List<UserDashboardResponse> assignedUsers) {
        private static TaskDashboardResponse from(Task task) {
            Set<User> assignedUsers = task.getAssignedUsers();
            return new TaskDashboardResponse(task.getId(), task.getTitle(), task.getStatus(), assignedUsers.stream()
                    .map(user -> new UserDashboardResponse(user.getUserName())).collect(Collectors.toList()));
        }
    }

    private record UserDashboardResponse(String userName) { }

}
