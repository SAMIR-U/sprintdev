package edu.uptc.swi.sprintdev.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import edu.uptc.swi.sprintdev.domain.User;
import jakarta.servlet.http.HttpSession;


@Controller
@RequestMapping("/workspace")
public class DashboardController extends AbstractController{

    @GetMapping("/dashboard")
    public String loadDashboardPage(@RequestParam int sprintId,
                                HttpSession session
    ) {
        User user = autenticatedUserIn(session);
        if (user == null) {
            return "redirect:/login";
        }

        return "dashboard";
    }
    
}
