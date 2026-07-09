package edu.uptc.swi.sprintdev.controller;

import java.time.LocalDate;
import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import edu.uptc.swi.sprintdev.controller.utils.SessionUtlis;
import edu.uptc.swi.sprintdev.domain.Sprint;
import edu.uptc.swi.sprintdev.domain.User;
import edu.uptc.swi.sprintdev.service.interfaces.ISprintService;
import jakarta.servlet.http.HttpSession;

@Controller
@RequestMapping("/workspace")
public class SprintController {

    private final ISprintService sprintService;

    public SprintController(ISprintService sprintService) {
        this.sprintService = sprintService;
    }

    @GetMapping("/")
    public String loadSprints(HttpSession session) {
        User user = SessionUtlis.autenticatedUserIn(session);
        if (user == null) {
            return "redirect:/login";
        }

        List<Sprint> sprints = sprintService.obtainMySprints(user.getId());
        session.setAttribute("sprints", sprints);

        return "workspace";
    }

    @PostMapping("/createsprint")
    public String createSprint(@RequestParam String name,
                               @RequestParam String goal,
                               @RequestParam LocalDate startDate,
                               @RequestParam LocalDate endDate,
                               HttpSession session) {

        User creator = SessionUtlis.autenticatedUserIn(session);
        if (creator == null) {
            return "redirect:/login";
        }
        
        Sprint sprint = new Sprint();
        sprint.setName(name);
        sprint.setGoal(goal);
        sprint.setStartDate(startDate);
        sprint.setEndDate(endDate);
        sprint.setCreator(creator);

        if (sprintService.createSprint(sprint)) {
            SessionUtlis.operSuccessMsg(session, "createsprint");
        } else {
            SessionUtlis.operfailMsg(session, "createsprint");
        }

        return "redirect:/workspace";
    }

    @GetMapping("/sprint")
    public String accestToSprint(@RequestParam int sprintId,
                              HttpSession session) {
        
        User user = SessionUtlis.autenticatedUserIn(session);
        if (user == null) {
            return "redirect:/login";
        }

        Sprint sprint = sprintService.findSprintById(sprintId, user.getId());
        session.setAttribute("sprint", sprint);

        return "sprint";
    }
}