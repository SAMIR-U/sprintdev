package edu.uptc.swi.sprintdev.controller;

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
    public ResponseEntity<Sprint> isLastVersion(@RequestParam int sprintId,
                            @RequestParam int version,
                            HttpSession session
    ) {
        User user = autenticatedUserIn(session);
        if (user==null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        try {
            Sprint sprint = sprintService.findSprintById(sprintId, user.getId());
            if (equalSprintVersion(sprint.getVersion(), version)) {
                return ResponseEntity.noContent().build();
            }
            return ResponseEntity.ok(sprint);
        } catch (EntityNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        } catch(UserDontHavePermissionException e){
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }
    }

    @PostMapping("task/editstatus")
    public ResponseEntity<Boolean> editStatus(@RequestParam int taskId,
                                @RequestParam TaskStatus taskStatus,
                                HttpSession session
    ) {
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
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        } catch(UserDontHavePermissionException e){
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        } catch (StatusTaskIsNotPossibleToChangeException e) {
            return ResponseEntity.status(HttpStatus.CONFLICT).build();
        }
    }
    
    private boolean equalSprintVersion(int sprint, int version){
        return sprint>0&&sprint==version;
    }

}
