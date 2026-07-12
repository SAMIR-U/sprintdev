package edu.uptc.swi.sprintdev.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import edu.uptc.swi.sprintdev.service.interfaces.IUserService;
import jakarta.servlet.http.HttpSession;

/**
 * Controller responsible for handling user authentication,
 * including displaying the login form and processing login attempts.
 */
@Controller
public class LoginController extends AbstractController {

    private final IUserService userService;

    /**
     * Creates a new login controller with the required user service.
     *
     * @param userService the service used to authenticate users and retrieve
     *                    their information
     */
    public LoginController(IUserService userService) {
        this.userService = userService;
    }

    /**
     * Displays the login page to the user.
     *
     * @return the view name for the login page
     */
    @GetMapping("/login")
    public String loginPage() {
        return "login";
    }

    /**
     * Processes the login form submission and authenticates the user.
     *
     * @param user the username entered by the user
     * @param password the password entered by the user
     * @param session the current HTTP session used to store the authenticated user
     * @param redirect object used to add flash messages for the next request
     * @return a redirect to the workspace page on success, or back to the login page on failure
     */
    @PostMapping("/login")
    public String login(@RequestParam String user,
                        @RequestParam String password,
                        HttpSession session,
                        RedirectAttributes redirect) {

        if (userService.loginUser(user, password)) {
            setAutenticatedUserIn(session, redirect, userService.obtainUserByUsername(user));
            return "redirect:/workspace";
        }
        operfailMsg(redirect, "login");
        return "redirect:/login";
    }
}
