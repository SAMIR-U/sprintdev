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

/**
 * Controller responsible for managing task-related operations
 * within a sprint, including loading the backlog, creating,
 * updating, and deleting tasks.
 */
@Controller
@RequestMapping("/workspace")
public class TaskController extends AbstractController{
    private final ISprintTaskService sprintTaskService;
    private final ISprintService sprintService;
    private final IUserService userService;

    /**
     * Creates a task controller with the required services.
     *
     * @param sprintTaskService the service used to manage sprint tasks
     * @param sprintService the service used to manage sprints
     * @param userService the service used to retrieve user information
     */
    public TaskController(ISprintTaskService sprintTaskService, ISprintService sprintService, IUserService userService) {
        this.sprintTaskService = sprintTaskService;
        this.sprintService = sprintService;
        this.userService = userService;
    }

    /**
     * Loads the backlog page for the specified sprint and stores sprint data in the session.
     *
     * @param sprintId the ID of the sprint whose backlog should be loaded
     * @param session the HTTP session used to verify authentication and save sprint data
     * @param redirect redirect attributes used to send flash messages when needed
     * @return the backlog view name or a redirect to login if the user is not authenticated
     */
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

    /**
     * Creates a new task in the specified sprint based on the submitted form data.
     *
     * @param taskForm the task form data provided by the user
     * @param session the HTTP session used to verify authentication
     * @param redirect redirect attributes used to add flash messages for the next request
     * @return a redirect to the sprint backlog page after attempting to create the task
     */
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

    /**
     * Updates an existing task with the provided title and description.
     *
     * @param sprintid the ID of the sprint that contains the task
     * @param taskId the ID of the task to update
     * @param title the new title for the task
     * @param description the new description for the task
     * @param session the HTTP session used to verify authentication
     * @param redirect redirect attributes used to add flash messages for the next request
     * @return a redirect to the sprint backlog after attempting to update the task
     */
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

    /**
     * Deletes a task from the sprint if the authenticated user has permission.
     *
     * @param sprintid the ID of the sprint that contains the task
     * @param taskId the ID of the task to delete
     * @param session the HTTP session used to verify authentication
     * @param redirect redirect attributes used to add flash messages for the next request
     * @return a redirect to the sprint backlog after attempting to delete the task
     */
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
    
    /**
     * Retrieves User entities for the given list of usernames.
     *
     * @param userNames the usernames to resolve to User objects
     * @return a set of User entities that exist for the provided usernames
     */
    private Set<User> obtainUsers(List<String> userNames) {
        Set<User> users = new HashSet<>();
        for (String userName : userNames) {
            User user = userService.obtainUserByUsername(userName);
            if (user!=null) {
                users.add(user);
            }
        }
        return users;
    }

    /**
     * Builds a Task entity from the provided form data and authenticated creator.
     *
     * @param taskForm the form data submitted by the user
     * @param user the authenticated user creating the task
     * @return a Task object populated with title, description, assigned users, and sprint
     */
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
