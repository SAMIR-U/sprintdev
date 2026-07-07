package edu.uptc.swi.sprintdev.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;

import jakarta.servlet.http.HttpSession;

@Controller
public class LogoutController {
    
    @PostMapping("/logout")
    public String logout(HttpSession session) {
        session.removeAttribute("user");
        session.removeAttribute("registmessage");
        session.removeAttribute("loginmessage");
        return "redirect:/";
    }
}
