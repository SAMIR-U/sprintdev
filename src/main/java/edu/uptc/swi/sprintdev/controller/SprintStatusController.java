package edu.uptc.swi.sprintdev.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import edu.uptc.swi.sprintdev.domain.User;
import edu.uptc.swi.sprintdev.exceptions.TheListIsFullException;
import edu.uptc.swi.sprintdev.exceptions.UserAlreadyExistInListException;
import edu.uptc.swi.sprintdev.exceptions.UserDontHavePermissionException;
import edu.uptc.swi.sprintdev.service.interfaces.ISprintService;
import jakarta.servlet.http.HttpSession;

/**
 * Controller responsible for changing sprint status.
 * It exposes endpoints to activate or close a sprint and handles authorization checks.
 */
@Controller
@RequestMapping("/workspace")
public class SprintStatusController extends AbstractController{
    private final ISprintService sprintService;

    public SprintStatusController(ISprintService sprintService) {
        this.sprintService = sprintService;
    }

    /**
     * Activates a sprint if the authenticated user has permission.
     *
     * @param sprintId the ID of the sprint to activate
     * @param session the current HTTP session used to verify the authenticated user
     * @param redirect redirect attributes used to add flash messages to the next request
     * @return a redirect to the sprint details page after the operation
     */
    @PostMapping("/sprint/active")
    public String activeSprint(@RequestParam int sprintId,
                               HttpSession session,
                               RedirectAttributes redirect) {

        User creator = autenticatedUserIn(session);
        if (creator == null) {
            return "redirect:/login";
        }
        try {
            if (sprintService.activateSprint(sprintId, creator.getId())) {
                operSuccessMsg(redirect, "activesprint");
            } else {
                operfailMsg(redirect, "activesprint");
            }
        } catch (UserDontHavePermissionException| UserAlreadyExistInListException| TheListIsFullException e) {
            operfailMsg(redirect, "activesprint", e.getMessage());
        }
        
        return "redirect:/workspace/sprint?sprintId="+sprintId;
    }

    /**
     * Closes a sprint if the authenticated user has permission.
     *
     * @param sprintId the ID of the sprint to close
     * @param session the current HTTP session used to verify the authenticated user
     * @param redirect redirect attributes used to add flash messages to the next request
     * @return a redirect to the sprint details page after the operation
     */
    @PostMapping("/sprint/close")
    public String closeSprint(@RequestParam int sprintId,
                               HttpSession session,
                               RedirectAttributes redirect) {

        User creator = autenticatedUserIn(session);
        if (creator == null) {
            return "redirect:/login";
        }

        try {
            if (sprintService.closeSprint(sprintId, creator.getId())) {
                operSuccessMsg(redirect, "closesprint");
            } else {
                operfailMsg(redirect, "closesprint");
            }
        } catch (UserDontHavePermissionException e) {
            operfailMsg(redirect, "closesprint", e.getMessage());
        }

        return "redirect:/workspace/sprint?sprintId="+sprintId;
    }
}
