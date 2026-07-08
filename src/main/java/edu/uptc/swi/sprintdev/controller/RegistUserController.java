package edu.uptc.swi.sprintdev.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import edu.uptc.swi.sprintdev.controller.utils.SessionUtlis;
import edu.uptc.swi.sprintdev.domain.User;
import edu.uptc.swi.sprintdev.service.interfaces.IUserService;
import jakarta.servlet.http.HttpSession;

@Controller
public class RegistUserController {
    private final IUserService userService;

    public RegistUserController(IUserService userService) {
        this.userService = userService;
    }

    @GetMapping("/registuser")
    public String loginPage() {
        return "registuser";
    }

    @PostMapping("/registuser")
    public String login(@RequestParam String user,
                        @RequestParam String password,
                        HttpSession session) {

        User userObj = new User();
        userObj.setUserName(user);
        userObj.setPassword(password);

        if (userService.registerUser(userObj)) {
            SessionUtlis.operSuccessMsg(session, "regist");
            SessionUtlis.setAutenticatedUserIn(session, userService.obtainUserByUsername(user));
            return "redirect:/mainmenu";
        }
        SessionUtlis.operfailMsg(session, "regist");
        session.setAttribute("registmessage", "fail");
        return "registuser";
    }
}
