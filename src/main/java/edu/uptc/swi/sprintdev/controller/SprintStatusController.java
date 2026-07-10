package edu.uptc.swi.sprintdev.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import edu.uptc.swi.sprintdev.domain.User;
import edu.uptc.swi.sprintdev.service.interfaces.ISprintService;
import jakarta.servlet.http.HttpSession;

@Controller
@RequestMapping("/workspace")
public class SprintStatusController extends AbstractController{
    private final ISprintService sprintService;

    public SprintStatusController(ISprintService sprintService) {
        this.sprintService = sprintService;
    }

    @PostMapping("/sprint/active")
    public String activeSprint(@RequestParam int sprintId,
                               HttpSession session) {

        User creator = autenticatedUserIn(session);
        if (creator == null) {
            return "redirect:/login";
        }
        if (sprintService.activateSprint(sprintId, creator.getId())) {
            operSuccessMsg(session, "activesprint");
        } else {
            operfailMsg(session, "activesprint");
        }

        return "redirect:/workspace/sprint?sprintId="+sprintId;
    }

    @PostMapping("/sprint/close")
    public String closeSprint(@RequestParam int sprintId,
                               HttpSession session) {

        User creator = autenticatedUserIn(session);
        if (creator == null) {
            return "redirect:/login";
        }
        if (sprintService.closeSprint(sprintId, creator.getId())) {
            operSuccessMsg(session, "activesprint");
        } else {
            operfailMsg(session, "activesprint");
        }

        return "redirect:/workspace/sprint?sprintId="+sprintId;
    }
}
