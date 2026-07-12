package edu.uptc.swi.sprintdev.controller;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import edu.uptc.swi.sprintdev.domain.Sprint;
import edu.uptc.swi.sprintdev.domain.Task;
import edu.uptc.swi.sprintdev.domain.User;
import edu.uptc.swi.sprintdev.exceptions.SprintIsClosedException;
import edu.uptc.swi.sprintdev.exceptions.TheListNeedAtleastOneTaskException;
import edu.uptc.swi.sprintdev.exceptions.UserDontHavePermissionException;
import edu.uptc.swi.sprintdev.net.TaskForm;
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
                            HttpSession session,
                            RedirectAttributes redirect) {
        User user = autenticatedUserIn(session);
        if (user == null) {
            return "redirect:/login";
        }
        try {
            Sprint sprint = sprintService.findSprintById(sprintId, user.getId());
            List<User> readers = sprintService.findAllReadersSprint(sprintId, user.getId());
            List<Task> tasks = sprintService.findAllSprintTasks(sprintId, user.getId());

            session.setAttribute("sprint", sprint);
            session.setAttribute("tasks", tasks);
            session.setAttribute("readers", readers);
        } catch (UserDontHavePermissionException e) {
            operfailMsg(redirect, "backlog", e.getMessage());
        }
        return "backlog";
    }

    @PostMapping("/createtask")
    public String createTask(@ModelAttribute TaskForm taskForm,
                        HttpSession session,
                        RedirectAttributes redirect) {
                            
        User user = autenticatedUserIn(session);
        if (user == null) {
            return "redirect:/login";
        }

        try {
            Task task = buildTask(taskForm, user);
            
            if (sprintTaskService.createTask(task,user.getId())) {
                operSuccessMsg(redirect, "createtask");
            }else{
                operfailMsg(redirect, "createtask");
            }
        } catch (UserDontHavePermissionException|SprintIsClosedException e) {
            operfailMsg(redirect, "createtask", e.getMessage());
        }
        return "redirect:/workspace/backlog?sprintId="+taskForm.getSprintid();
    }

    @PostMapping("/updatetaks")
    public String updateTask(@RequestParam int sprintid,
                        @RequestParam int taskId,
                        @RequestParam String title,
                        @RequestParam String description,
                        HttpSession session,
                        RedirectAttributes redirect) {

        User user = autenticatedUserIn(session);
        if (user == null) {
            return "redirect:/login";
        }

        Task task = sprintTaskService.findTaskById(taskId);
        task.setTitle(title);
        task.setDescription(description);

        try {
            if (sprintTaskService.updateTask(task, user.getId())) {
                operSuccessMsg(redirect, "edittask");
            }else{
                operfailMsg(redirect, "edittask");
            }
        } catch (UserDontHavePermissionException e) {
            operfailMsg(redirect, "edittask", e.getMessage());
        }
        return "redirect:/workspace/backlog?sprintId="+sprintid;
    }

    @PostMapping("/deletetaks")
    public String deleteTask(@RequestParam int sprintid,
                        @RequestParam int taskId,
                        HttpSession session,
                        RedirectAttributes redirect) {

        User user = autenticatedUserIn(session);
        if (user == null) {
            return "redirect:/login";
        }
        
        Task task = sprintTaskService.findTaskById(taskId);
        try {
            if (sprintTaskService.deleteTask(task, user.getId())) {
                operSuccessMsg(redirect, "deletetask");
            }else{
                operfailMsg(redirect, "deletetask");
            }
        } catch (UserDontHavePermissionException|TheListNeedAtleastOneTaskException e) {
            operfailMsg(redirect, "deletetask", e.getMessage());
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

    private Task buildTask(TaskForm taskForm, User user) {
        Sprint sprint = sprintService.findSprintById(taskForm.getSprintid(), user.getId());

        Task task = new Task();
        Set<User> assignedUsers = obtainUsers(taskForm.getAssignedUserNames());
        task.setTitle(taskForm.getTitle());
        task.setDescription(taskForm.getDescription());
        task.setAssignedUsers(assignedUsers);
        task.setSprint(sprint);
        return task;
    }
}
