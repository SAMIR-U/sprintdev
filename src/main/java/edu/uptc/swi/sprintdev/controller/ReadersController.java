package edu.uptc.swi.sprintdev.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestParam;

import edu.uptc.swi.sprintdev.domain.User;
import edu.uptc.swi.sprintdev.exceptions.TheListIsFullException;
import edu.uptc.swi.sprintdev.exceptions.UserAlreadyExistInListException;
import edu.uptc.swi.sprintdev.exceptions.UserDontHavePermissionException;
import edu.uptc.swi.sprintdev.service.interfaces.ISprintService;
import edu.uptc.swi.sprintdev.service.interfaces.IUserService;
import jakarta.servlet.http.HttpSession;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/workspace")
public class ReadersController extends AbstractController{
    private final ISprintService sprintService;
    private final IUserService userService;

    public ReadersController(ISprintService sprintService, IUserService userService) {
        this.sprintService = sprintService;
        this.userService = userService;
    }
    
    @PostMapping("/addreader")
    public String addReaderToSprints(@RequestParam int sprintId,
                                @RequestParam String readerName,
                                HttpSession session) {
        User user = autenticatedUserIn(session);
        if (user == null) {
            return "redirect:/login";
        }

        User reader = userService.obtainUserByUsername(readerName);
        try {
            if (sprintService.addReaderToSprint(sprintId, user.getId(), reader)) {
                operSuccessMsg(session, "addreader");
            }else{
                operfailMsg(session, "addreader");
            }
        } catch (UserDontHavePermissionException|UserAlreadyExistInListException|TheListIsFullException e) {
            operfailMsg(session, "addreader", e.getMessage());
        }
        return "redirect:/workspace/sprint?sprintId="+sprintId;
    }
    
}
