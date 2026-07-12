package edu.uptc.swi.sprintdev.controller;

import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import edu.uptc.swi.sprintdev.domain.User;
import jakarta.servlet.http.HttpSession;

public abstract class AbstractController {

    protected User autenticatedUserIn(HttpSession session){
        User user = (User) session.getAttribute("user");
        return user;
    }
    protected void setAutenticatedUserIn(HttpSession session, RedirectAttributes redirect, User user){
        operSuccessMsg(redirect, "login");
        session.setAttribute("user", user);
    }
    protected void operSuccessMsg(RedirectAttributes redirect, String title){
        redirect.addFlashAttribute(title, "success");              
    }
    protected void operfailMsg(RedirectAttributes redirect, String title){
        redirect.addFlashAttribute(title, "fail");              
    }
    protected void operfailMsg(RedirectAttributes redirect, String title, String exceptionMessage){
        redirect.addFlashAttribute(title, exceptionMessage);
    }


}
