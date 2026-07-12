package edu.uptc.swi.sprintdev.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import edu.uptc.swi.sprintdev.domain.User;
import edu.uptc.swi.sprintdev.service.interfaces.IUserService;
import jakarta.servlet.http.HttpSession;

/**
 * Controller responsible for handling user registration,
 * including displaying the registration page and processing
 * registration requests.
 */
@Controller
public class RegistUserController extends AbstractController {
    private final IUserService userService;

    /**
     * Creates a registration controller with the required user service.
     *
     * @param userService the service used to register users and retrieve
     *                    their information
     */
    public RegistUserController(IUserService userService) {
        this.userService = userService;
    }

    /**
     * Displays the registration page.
     *
     * @return the view name for the registration form
     */
    @GetMapping("/registuser")
    public String registPage() {
        return "registuser";
    }

    /**
     * Processes the registration form submission and creates a new user account.
     *
     * @param user the username entered by the new user
     * @param password the password entered by the new user
     * @param session the current HTTP session used to store the authenticated user
     * @param redirect object used to add flash messages for the next request
     * @return a redirect to the workspace page on success, or back to the registration page on failure
     */
    @PostMapping("/registuser")
    public String regist(@RequestParam String user,
                         @RequestParam String password,
                         HttpSession session,
                         RedirectAttributes redirect) {

        User userObj = new User();
        userObj.setUserName(user);
        userObj.setPassword(password);

        if (userService.registerUser(userObj)) {
            operSuccessMsg(redirect, "regist");
            setAutenticatedUserIn(session, redirect, userService.obtainUserByUsername(user));
            return "redirect:/workspace";
        }
        operfailMsg(redirect, "regist");
        return "redirect:/registuser";
    }
}
