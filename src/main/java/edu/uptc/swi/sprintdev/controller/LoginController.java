package edu.uptc.swi.sprintdev.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import edu.uptc.swi.sprintdev.service.interfaces.IUserService;
import jakarta.servlet.http.HttpSession;

@Controller
public class LoginController extends AbstractController{

    private final IUserService userService;

    public LoginController(IUserService userService) {
        this.userService = userService;
    }

    @GetMapping("/login")
    public String loginPage() {
        return "login";
    }

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
