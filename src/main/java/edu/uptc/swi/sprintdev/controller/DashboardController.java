package edu.uptc.swi.sprintdev.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import edu.uptc.swi.sprintdev.domain.User;
import jakarta.servlet.http.HttpSession;

/**
 * Controller responsible for handling dashboard-related requests
 * within a workspace.
 */
@Controller
@RequestMapping("/workspace")
public class DashboardController extends AbstractController {

    /**
     * Loads the dashboard page for an authenticated user.
     *
     * @param sprintId the identifier of the requested sprint
     * @param session the current HTTP session
     * @return the dashboard view if a user is authenticated;
     *         otherwise, a redirect to the login page
     */
    @GetMapping("/dashboard")
    public String loadDashboardPage(@RequestParam int sprintId,
                                    HttpSession session) {
        User user = autenticatedUserIn(session);
        if (user == null) {
            return "redirect:/login";
        }

        return "dashboard";
    }
}
