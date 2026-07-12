package edu.uptc.swi.sprintdev.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;

import jakarta.servlet.http.HttpSession;

/**
 * Controller responsible for handling user logout operations.
 */
@Controller
public class LogoutController extends AbstractController {

    /**
     * Logs out the current user by removing the authenticated user from the session
     * and redirects the user to the home page.
     *
     * @param session the current HTTP session containing the authenticated user
     * @return a redirect to the home page after logout
     */
    @PostMapping("/logout")
    public String logout(HttpSession session) {
        session.removeAttribute("user");
        return "redirect:/";
    }
}
