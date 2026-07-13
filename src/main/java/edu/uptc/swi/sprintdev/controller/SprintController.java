package edu.uptc.swi.sprintdev.controller;

import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import edu.uptc.swi.sprintdev.domain.Sprint;
import edu.uptc.swi.sprintdev.domain.User;
import edu.uptc.swi.sprintdev.service.interfaces.ISprintService;
import jakarta.servlet.http.HttpSession;
import edu.uptc.swi.sprintdev.exceptions.InvalidDateException;
import edu.uptc.swi.sprintdev.exceptions.UserNotFoundException;
import edu.uptc.swi.sprintdev.net.SprintForm;

/**
 * Controller responsible for managing sprint-related operations
 * within the workspace, including loading sprints, creating new
 * sprints, and accessing sprint details.
 */
@Controller
@RequestMapping("/workspace")
public class SprintController extends AbstractController{

    private final ISprintService sprintService;

    public SprintController(ISprintService sprintService) {
        this.sprintService = sprintService;
    }
    
    /**
     * Loads the workspace with the sprints that belong to the
     * authenticated user.
     *
     * @param session the HTTP session used to verify authentication and save sprint data
     * @return the workspace view when the user is authenticated, otherwise a login redirect
     */
    @GetMapping("")
    public String loadSprints(HttpSession session) {
        User user = autenticatedUserIn(session);
        if (user == null) {
            return "redirect:/login";
        }

        List<Sprint> sprints = sprintService.obtainMySprints(user.getId());
        session.setAttribute("sprints", sprints);

        return "workspace";
    }

    /**
     * Creates a new sprint from the submitted form and redirects back to the workspace.
     *
     * @param sprintForm the form object containing sprint details entered by the user
     * @param session the HTTP session used to verify authentication
     * @param redirect the redirect attributes used to send flash messages to the next request
     * @return a redirect to the workspace after attempting to create the sprint
     */
    @PostMapping("/createsprint")
    public String createSprint(@ModelAttribute SprintForm sprintForm,
                               HttpSession session,
                               RedirectAttributes redirect) {

        User user = autenticatedUserIn(session);
        if (user == null) {
            return "redirect:/login";
        }
        
        Sprint sprint = buildSprint(sprintForm, user);

        try {
            if (sprintService.createSprint(sprint)) {
                operSuccessMsg(redirect, "createsprint");
            } else {
                redirect.addFlashAttribute("sprintForm", sprintForm);                
                operfailMsg(redirect, "createsprint");
            }
        } catch (InvalidDateException e) {
            operfailMsg(redirect, "createsprint", e.getMessage());
            redirect.addFlashAttribute("sprintForm", sprintForm);                
        }
        return "redirect:/workspace";
    }

    /**
     * Opens the sprint details page if the authenticated user is authorized to view it.
     *
     * @param sprintId the ID of the sprint to access
     * @param session the HTTP session used to verify authentication
     * @param redirect the redirect attributes used to send error messages
     * @return the sprint view, or a login redirect if the user is not authenticated
     */
    @GetMapping("/sprint")
    public String accestToSprint(@RequestParam int sprintId,
                              HttpSession session,
                              RedirectAttributes redirect) {
        
        User user = autenticatedUserIn(session);
        if (user == null) {
            return "redirect:/login";
        }

        try {
            Sprint sprint = sprintService.findSprintById(sprintId, user.getId());
            List<User> readers = sprintService.findAllReadersSprint(sprintId, user.getId());
            session.setAttribute("sprint", sprint);
            session.setAttribute("readers", readers);
        }catch (UserNotFoundException e) {
            operfailMsg(redirect, "loadsprint", e.getMessage());
        }
        return "sprint";
    }

    /**
     * Builds a Sprint entity from the submitted form data and creator information.
     *
     * @param sprintForm the form data provided by the user
     * @param creator the authenticated user who will be marked as the sprint creator
     * @return a Sprint object populated with the form values and creator
     */
    private Sprint buildSprint(SprintForm sprintForm, User creator) {
        Sprint sprint =new Sprint();
        sprint.setName(sprintForm.getName());
        sprint.setGoal(sprintForm.getGoal());
        sprint.setStartDate(sprintForm.getStartDate());
        sprint.setEndDate(sprintForm.getEndDate());
        sprint.setCreator(creator);
        return sprint;
    }
}