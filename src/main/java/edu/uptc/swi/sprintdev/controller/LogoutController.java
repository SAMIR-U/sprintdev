package edu.uptc.swi.sprintdev.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;

import edu.uptc.swi.sprintdev.controller.utils.SessionUtlis;
import jakarta.servlet.http.HttpSession;

@Controller
public class LogoutController {
    
    @PostMapping("/logout")
    public String logout(HttpSession session) {
        SessionUtlis.logout(session);
        return "redirect:/";
    }
}
