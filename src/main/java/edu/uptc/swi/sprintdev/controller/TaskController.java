package edu.uptc.swi.sprintdev.controller;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import edu.uptc.swi.sprintdev.domain.Sprint;
import edu.uptc.swi.sprintdev.domain.Task;
import edu.uptc.swi.sprintdev.domain.User;
import edu.uptc.swi.sprintdev.service.interfaces.ISprintService;
import edu.uptc.swi.sprintdev.service.interfaces.ISprintTaskService;
import edu.uptc.swi.sprintdev.service.interfaces.IUserService;
import jakarta.servlet.http.HttpSession;

@Controller
@RequestMapping("/workspace")
public class TaskController extends AbstractController{
    private final ISprintTaskService sprintTaskService;
    private final ISprintService sprintService;
    private final IUserService userService;

    public TaskController(ISprintTaskService sprintTaskService, ISprintService sprintService, IUserService userService) {
        this.sprintTaskService = sprintTaskService;
        this.sprintService = sprintService;
        this.userService = userService;
    }

    @GetMapping("/backlog")
    public String loadbacklog(@RequestParam int sprintId,
                            HttpSession session
    ) {
        User user = autenticatedUserIn(session);
        if (user == null) {
            return "redirect:/login";
        }

        List<User> readers = sprintService.findAllReadersSprint(sprintId, user.getId());
        List<Task> tasks = sprintService.findAllSprintTasks(sprintId, sprintId);
        session.setAttribute("tasks", tasks);
        session.setAttribute("readers", readers);
        return "backlog";
    }

    @PostMapping("/createtask")//dejar el no tienes permisos html
    public String createTask(@RequestParam int sprintid,
                        @RequestParam String title,
                        @RequestParam String description,
                        @RequestParam List<String> assignedUserNames,
                        HttpSession session) {
                            
                            User user = autenticatedUserIn(session);
        if (user == null) {
            return "redirect:/login";
        }
        Sprint sprint = sprintService.findSprintById(sprintid, user.getId());

        Task task = new Task();
        Set<User> assignedUsers = obtainUsers(assignedUserNames);
        task.setTitle(title);
        task.setDescription(description);
        task.setAssignedUsers(assignedUsers);
        task.setSprint(sprint);

        if (sprintTaskService.createTask(task,user.getId())) {
            operSuccessMsg(session, "createtask");
        }else{
            operfailMsg(session, "createtask");
        }
        return "redirect:/workspace/backlog?sprintId="+sprintid;
    }

    @PostMapping("/updatetaks")
    public String updateTask(@RequestParam int sprintid,
                        @RequestParam int taskId,
                        @RequestParam String title,
                        @RequestParam String description,
                        HttpSession session) {

        User user = autenticatedUserIn(session);
        if (user == null) {
            return "redirect:/login";
        }

        Task task = sprintTaskService.findTaskById(taskId);
        task.setTitle(title);
        task.setDescription(description);

        if (sprintTaskService.updateTask(task, user.getId())) {
            operSuccessMsg(session, "edittask");
        }else{
            operfailMsg(session, "edittask");
        }
        return "redirect:/workspace/backlog?sprintId="+sprintid;
    }

    @PostMapping("/deletetaks")
    public String deleteTask(@RequestParam int sprintid,
                        @RequestParam int taskId,
                        HttpSession session) {

        User user = autenticatedUserIn(session);
        if (user == null) {
            return "redirect:/login";
        }

        Task task = new Task();
        task.setId(taskId);

        if (sprintTaskService.deleteTask(task, user.getId())) {
            operSuccessMsg(session, "deletetask");
        }else{
            operfailMsg(session, "deletetask");
        }
        return "redirect:/workspace/backlog?sprintId="+sprintid;
    }

    private Set<User> obtainUsers(List<String> userNames) {
        Set<User> users = new HashSet<User>();
        for (String userName : userNames) {
            User user = userService.obtainUserByUsername(userName);
            if (user!=null) {
                users.add(user);
            }
        }
        return users;
    }
}
