package edu.uptc.swi.sprintdev.controller;

import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import edu.uptc.swi.sprintdev.domain.Sprint;
import edu.uptc.swi.sprintdev.domain.User;
import edu.uptc.swi.sprintdev.service.interfaces.ISprintService;
import jakarta.servlet.http.HttpSession;
import edu.uptc.swi.sprintdev.exceptions.InvalidDateException;
import edu.uptc.swi.sprintdev.exceptions.UserNotFoundException;
import edu.uptc.swi.sprintdev.net.SprintForm;

@Controller
@RequestMapping("/workspace")
public class SprintController extends AbstractController{

    private final ISprintService sprintService;

    public SprintController(ISprintService sprintService) {
        this.sprintService = sprintService;
    }
    
    @GetMapping("")
    public String loadSprints(HttpSession session) {
        User user = autenticatedUserIn(session);
        if (user == null) {
            return "redirect:/login";
        }

        List<Sprint> sprints = sprintService.obtainMySprints(user.getId());
        session.setAttribute("sprints", sprints);

        return "workspace";
    }

    @PostMapping("/createsprint")
    public String createSprint(@ModelAttribute SprintForm sprintForm,
                               HttpSession session,
                               RedirectAttributes redirectAttributes) {

        User creator = autenticatedUserIn(session);
        if (creator == null) {
            return "redirect:/login";
        }
        
        Sprint sprint = new Sprint();
        sprint.setName(sprintForm.getName());
        sprint.setGoal(sprintForm.getGoal());
        sprint.setStartDate(sprintForm.getStartDate());
        sprint.setEndDate(sprintForm.getEndDate());
        sprint.setCreator(creator);

        try {
            if (sprintService.createSprint(sprint)) {
                operSuccessMsg(session, "createsprint");
            } else {
                redirectAttributes.addFlashAttribute("sprintForm", sprintForm);                
                operfailMsg(session, "createsprint");
            }
        } catch (InvalidDateException e) {
            operfailMsg(session, "createsprint", e.getMessage());
            redirectAttributes.addFlashAttribute("sprintForm", sprintForm);                
        }
        return "redirect:/workspace";
    }

    @GetMapping("/sprint")
    public String accestToSprint(@RequestParam int sprintId,
                              HttpSession session) {
        
        User user = autenticatedUserIn(session);
        if (user == null) {
            return "redirect:/login";
        }

        try {
            Sprint sprint = sprintService.findSprintById(sprintId, user.getId());
            List<User> readers = sprintService.findAllReadersSprint(sprintId, user.getId());
            session.setAttribute("sprint", sprint);
            session.setAttribute("readers", readers);
        }catch (UserNotFoundException e) {
            operfailMsg(session, "loadsprint", e.getMessage());
        }
        return "sprint";
    }
}