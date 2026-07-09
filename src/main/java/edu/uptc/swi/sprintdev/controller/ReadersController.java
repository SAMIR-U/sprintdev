package edu.uptc.swi.sprintdev.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import edu.uptc.swi.sprintdev.controller.utils.SessionUtlis;
import edu.uptc.swi.sprintdev.domain.User;
import edu.uptc.swi.sprintdev.service.interfaces.ISprintService;
import edu.uptc.swi.sprintdev.service.interfaces.IUserService;
import jakarta.servlet.http.HttpSession;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/workspace")
public class ReadersController {
    private final ISprintService sprintService;
    private final IUserService userService;

    public ReadersController(ISprintService sprintService, IUserService userService) {
        this.sprintService = sprintService;
        this.userService = userService;
    }

    @GetMapping("/findreaders")
    @ResponseBody
    public ResponseEntity<List<User>> findReaders(@RequestParam String key,
                            HttpSession session) {
        User user = SessionUtlis.autenticatedUserIn(session);
        if (user == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        List<User> users = userService.findUserByKeyWord(key);
        return ResponseEntity.ok(users);
    }
    
    @PostMapping("/addreader")
    public String addReaderToSprints(@RequestParam int sprintId,
                                @RequestParam String readerName,
                                HttpSession session) {
        User user = SessionUtlis.autenticatedUserIn(session);
        if (user == null) {
            return "redirect:/login";
        }

        User reader = userService.obtainUserByUsername(readerName);
        if (sprintService.addReaderToSprint(sprintId, user.getId(), reader)) {
            SessionUtlis.operfailMsg(session, "addreader");
        }else{
            SessionUtlis.operSuccessMsg(session, "addreader");
        }
        return "redirect:/workspace/sprint";
    }
    
}
