package edu.uptc.swi.sprintdev.controller;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.web.bind.annotation.RestController;

import edu.uptc.swi.sprintdev.domain.Sprint;
import edu.uptc.swi.sprintdev.domain.Task;
import edu.uptc.swi.sprintdev.domain.TaskStatus;
import edu.uptc.swi.sprintdev.domain.User;
import edu.uptc.swi.sprintdev.exceptions.SprintIsNotActiveException;
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

/**
 * REST controller responsible for providing dashboard data and
 * handling task status updates within a sprint.
 */
@RestController
@RequestMapping("/api")
public class DashboardRestController extends AbstractController{
    private final ISprintService sprintService;
    private final ISprintTaskService sprintTaskService;

    /**
     * Constructs the controller with the required services.
     *
     * @param sprintService service to retrieve sprint data
     * @param sprintTaskService service to update sprint tasks
     */
    public DashboardRestController(ISprintService sprintService, ISprintTaskService sprintTaskService){
        this.sprintService = sprintService;
        this.sprintTaskService = sprintTaskService;
    }

    /**
     * Checks whether the provided client-side sprint version matches the server-side
     * version for the given sprint. If versions match, returns 204 No Content
     * (indicating no update is required); otherwise returns the full sprint payload.
     *
     * @param sprintId the id of the sprint to check
     * @param version the client-side version number to compare
     * @param session the HTTP session used to validate the authenticated user
     * @return 204 No Content when versions are equal, 200 with sprint payload when different,
     *         401 when unauthenticated, 404 when sprint is not found, or 403 when access is forbidden
     */
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

    /**
     * Updates the status of a task. Validates authentication and permission.
     * Returns 200 with the boolean result when successful, or an appropriate
     * error status and message when the operation fails.
     *
     * @param taskId the id of the task to update
     * @param taskStatus the new status to apply to the task
     * @param session the HTTP session used to validate the authenticated user
     * @return 200 with boolean result on success, 401/403/404/409 on failure with explanatory message
     */
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
        } catch (SprintIsNotActiveException e){
            return ResponseEntity.status(HttpStatus.CONFLICT).body(e.getMessage());
        }
    }

    /**
     * Compares sprint version numbers.
     *
     * @param sprint the current server-side sprint version
     * @param version the client-provided version to compare
     * @return true when the provided version is positive and equals the server version
     */
    private boolean equalSprintVersion(int sprint, int version){
        return sprint>0&&sprint==version;
    }

    /**
     * Response payload sent when the dashboard requires a full update.
     */
    private record SprintDashboardResponse(int version, List<TaskDashboardResponse> tasks) {
        private static SprintDashboardResponse from(Sprint sprint) {
            return new SprintDashboardResponse(sprint.getVersion(), sprint.getTasks().stream()
                    .map(TaskDashboardResponse::from).collect(Collectors.toList()));
        }
    }

    /**
     * DTO describing a task in the dashboard response.
     */
    private record TaskDashboardResponse(int id, String title, TaskStatus status, List<UserDashboardResponse> assignedUsers) {
        private static TaskDashboardResponse from(Task task) {
            Set<User> assignedUsers = task.getAssignedUsers();
            return new TaskDashboardResponse(task.getId(), task.getTitle(), task.getStatus(), assignedUsers.stream()
                    .map(user -> new UserDashboardResponse(user.getUserName())).collect(Collectors.toList()));
        }
    }

    /**
     * Simple DTO for user data returned in the dashboard response.
     */
    private record UserDashboardResponse(String userName) { }

}
