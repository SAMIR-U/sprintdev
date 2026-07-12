package edu.uptc.swi.sprintdev.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import edu.uptc.swi.sprintdev.domain.User;
import edu.uptc.swi.sprintdev.exceptions.TheListIsFullException;
import edu.uptc.swi.sprintdev.exceptions.UserAlreadyExistInListException;
import edu.uptc.swi.sprintdev.exceptions.UserDontHavePermissionException;
import edu.uptc.swi.sprintdev.service.interfaces.ISprintService;
import edu.uptc.swi.sprintdev.service.interfaces.IUserService;
import jakarta.servlet.http.HttpSession;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * Controller responsible for managing readers associated with a sprint.
 * It handles adding readers and redirecting the user after the operation.
 */
@Controller
@RequestMapping("/workspace")
public class ReadersController extends AbstractController {
    private final ISprintService sprintService;
    private final IUserService userService;

    public ReadersController(ISprintService sprintService, IUserService userService) {
        this.sprintService = sprintService;
        this.userService = userService;
    }
    
    /**
     * Adds a reader to a sprint if the current user is authenticated and authorized.
     *
     * @param sprintId the ID of the sprint to which the reader will be added
     * @param readerName the username of the user that will be added as a reader
     * @param session the current HTTP session used to verify authentication
     * @param redirect object used to send flash messages to the next request
     * @return a redirect to the sprint page after the operation is completed
     */
    @PostMapping("/addreader")
    public String addReaderToSprints(@RequestParam int sprintId,
                                @RequestParam String readerName,
                                HttpSession session,
                                RedirectAttributes redirect) {
        User user = autenticatedUserIn(session);
        if (user == null) {
            return "redirect:/login";
        }

        User reader = userService.obtainUserByUsername(readerName);
        try {
            if (sprintService.addReaderToSprint(sprintId, user.getId(), reader)) {
                operSuccessMsg(redirect, "addreader");
            } else {
                operfailMsg(redirect, "addreader");
            }
        } catch (UserDontHavePermissionException | UserAlreadyExistInListException | TheListIsFullException e) {
            operfailMsg(redirect, "addreader", e.getMessage());
        }
        return "redirect:/workspace/sprint?sprintId=" + sprintId;
    }
    
}
