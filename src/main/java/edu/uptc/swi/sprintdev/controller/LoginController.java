package edu.uptc.swi.sprintdev.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import edu.uptc.swi.sprintdev.service.IUserService;
import jakarta.servlet.http.HttpSession;

@Controller
public class LoginController {

    private final IUserService userService;

    public LoginController(IUserService userService) {
        this.userService = userService;
    }

    @GetMapping("/")
    public String loginPage() {
        return "index";
    }

    @PostMapping("/login")
    public String login(@RequestParam String user,
                        @RequestParam String password,
                        HttpSession session) {

        if (userService.loginUser(user, password)) {
            session.setAttribute("loginmessage", "success");
            session.setAttribute("user", userService.obtainUserByUsername(user));
            return "redirect:/mainmenu";
        }
        session.setAttribute("loginmessage", "fail");
        return "index";
    }
}
