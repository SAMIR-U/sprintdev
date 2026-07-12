package edu.uptc.swi.sprintdev.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import edu.uptc.swi.sprintdev.domain.User;
import edu.uptc.swi.sprintdev.service.interfaces.IUserService;
import jakarta.servlet.http.HttpSession;

@Controller
public class RegistUserController extends AbstractController{
    private final IUserService userService;

    public RegistUserController(IUserService userService) {
        this.userService = userService;
    }

    @GetMapping("/registuser")
    public String registPage() {
        return "registuser";
    }

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
            setAutenticatedUserIn(session, redirect,userService.obtainUserByUsername(user));
            return "redirect:/workspace";
        }
        operfailMsg(redirect, "regist");
        return "redirect:/registuser";
    }
}
