package edu.uptc.swi.sprintdev.controller;


import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import edu.uptc.swi.sprintdev.controller.utils.SessionUtlis;
import edu.uptc.swi.sprintdev.domain.Sprint;
import edu.uptc.swi.sprintdev.domain.Task;
import edu.uptc.swi.sprintdev.domain.TaskStatus;
import edu.uptc.swi.sprintdev.domain.User;
import edu.uptc.swi.sprintdev.service.interfaces.ISprintService;
import edu.uptc.swi.sprintdev.service.interfaces.ISprintTaskService;
import jakarta.servlet.http.HttpSession;

@Controller
@RequestMapping("/sprint")
public class TaskController {
    private final ISprintTaskService sprintTaskService;
    private final ISprintService sprintService;
    

    public TaskController(ISprintTaskService sprintTaskService, ISprintService sprintService) {
        this.sprintTaskService = sprintTaskService;
        this.sprintService = sprintService;
    }

    @GetMapping("/backlog")
    public String loadbacklog() {
        return "backlog";
    }

    @PostMapping("/createtask")
    public String createTask(@RequestParam int sprintId,
                        @RequestParam String title,
                        @RequestParam String description,
                        @RequestParam TaskStatus status,
                        HttpSession session) {

        User user = SessionUtlis.autenticatedUserIn(session);
        if (user == null) {
            return "redirect:/login";
        }
        Sprint sprint = sprintService.findSprintById(sprintId, user.getId());

        Task task = new Task();
        task.setTitle(title);
        task.setDescription(description);
        task.setSprint(sprint);
        task.setStatus(status);

        if (sprintTaskService.createTask(task)) {
            SessionUtlis.operSuccessMsg(session, "createtask");
            return "redirect:/backlog";
        }
        SessionUtlis.operfailMsg(session, "createtask");
        return "redirect:/backlog";
    }

    @PostMapping("/taskinfo")
    @ResponseBody
    public ResponseEntity<Task> obtainTaskInfo(@RequestParam int sprintId,
                        @RequestParam int taskId,
                        HttpSession session) {

        User user = SessionUtlis.autenticatedUserIn(session);
        if (user == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        Task task = sprintTaskService.findTaskById(taskId);

        if (task == null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
        return ResponseEntity.ok(task);
    }

    @PostMapping("/updatetaks")
    public String updateTask(@RequestParam int taskId,
                        @RequestParam String title,
                        @RequestParam String description,
                        @RequestParam TaskStatus status,
                        HttpSession session) {

        User user = SessionUtlis.autenticatedUserIn(session);
        if (user == null) {
            return "redirect:/login";
        }

        Task task = sprintTaskService.findTaskById(taskId);
        task.setTitle(title);
        task.setDescription(description);
        task.setStatus(status);

        if (sprintTaskService.updateTask(task)) {
            SessionUtlis.operSuccessMsg(session, "edittask");
            return "redirect:/backlog";
        }
        SessionUtlis.operfailMsg(session, "edittask");
        return "redirect:/backlog";
    }

    @PostMapping("/deletetaks")
    public String deleteTask(@RequestParam int taskId,
                        HttpSession session) {

        User user = SessionUtlis.autenticatedUserIn(session);
        if (user == null) {
            return "redirect:/login";
        }

        Task task = new Task();
        task.setId(taskId);

        if (sprintTaskService.deleteTask(task)) {
            SessionUtlis.operSuccessMsg(session, "deletetask");
            return "redirect:/backlog";
        }
        SessionUtlis.operfailMsg(session, "deletetask");
        return "redirect:/backlog";
    }
}
