package edu.uptc.swi.sprintdev.controller;

import java.time.LocalDate;
import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import edu.uptc.swi.sprintdev.domain.Sprint;
import edu.uptc.swi.sprintdev.domain.User;
import edu.uptc.swi.sprintdev.service.ISprintService;
import jakarta.servlet.http.HttpSession;

@Controller
public class SprintController {

    private final ISprintService sprintService;

    public SprintController(ISprintService sprintService) {
        this.sprintService = sprintService;
    }

    @GetMapping("/mainmenu")
    public String loadSprints(HttpSession session) {

        User user = (User) session.getAttribute("user");

        if (user == null) {
            return "index";
        }

        List<Sprint> sprints = sprintService.obtainMySprints(user.getId());

        session.setAttribute("sprints", sprints);

        return "mainmenu";
    }

    @PostMapping("/createsprint")
    public String createSprint(@RequestParam String name,
                               @RequestParam String goal,
                               @RequestParam LocalDate startDate,
                               @RequestParam LocalDate endDate,
                               HttpSession session) {

        User creator = (User) session.getAttribute("user");

        if (creator == null) {
            return "index";
        }

        Sprint sprint = new Sprint();
        sprint.setName(name);
        sprint.setGoal(goal);
        sprint.setStartDate(startDate);
        sprint.setEndDate(endDate);
        sprint.setCreator(creator);

        if (sprintService.createSprint(sprint)) {
            session.setAttribute("sprintmessage", "success");
        } else {
            session.setAttribute("sprintmessage", "fail");
        }

        return "redirect:/mainmenu";
    }
}