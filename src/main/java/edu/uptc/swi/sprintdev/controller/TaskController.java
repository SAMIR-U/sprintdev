package edu.uptc.swi.sprintdev.controller;

import java.time.LocalDateTime;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import edu.uptc.swi.sprintdev.controller.utils.SessionUtlis;
import edu.uptc.swi.sprintdev.domain.Task;
import edu.uptc.swi.sprintdev.domain.TaskStatus;
import edu.uptc.swi.sprintdev.domain.User;
import edu.uptc.swi.sprintdev.service.interfaces.ISprintTaskService;
import jakarta.servlet.http.HttpSession;

@Controller
@RequestMapping("/sprint")
public class TaskController {
    private final ISprintTaskService sprintTaskService;

    public TaskController(ISprintTaskService sprintTaskService) {
        this.sprintTaskService = sprintTaskService;
    }

    @GetMapping("/backlog")
    public String loadbacklog() {
        return "backlog";
    }

    @PostMapping("/createtask")
    public String login(@RequestParam int sprintId,
                        @RequestParam String title,
                        @RequestParam String description,
                        @RequestParam TaskStatus status,
                        HttpSession session) {

        User user = SessionUtlis.autenticatedUserIn(session);
        if (user == null) {
            return "redirect:/login";
        }

        Task task = new Task();
        task.setId(sprintId);
        task.setTitle(title);
        task.setDescription(description);
        task.setStatus(status);

        if (sprintTaskService.createTask(task)) {
            SessionUtlis.operSuccessMsg(session, "regist");
            return "redirect:/backlog";
        }
        SessionUtlis.operfailMsg(session, "regist");
        return "redirect:/backlog";
    }
}
