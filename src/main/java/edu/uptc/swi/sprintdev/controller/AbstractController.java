package edu.uptc.swi.sprintdev.controller;

import edu.uptc.swi.sprintdev.domain.User;
import jakarta.servlet.http.HttpSession;

public abstract class AbstractController {

    protected User autenticatedUserIn(HttpSession session){
        User user = (User) session.getAttribute("user");
        return user;
    }
    protected void setAutenticatedUserIn(HttpSession session, User user){
        operSuccessMsg(session, "login");
        session.setAttribute("user", user);
    }
    protected void operSuccessMsg(HttpSession session, String title){
        session.setAttribute(title, "success");      
    }
    protected void operfailMsg(HttpSession session, String title){
        session.setAttribute(title, "fail");
    }
    protected void operfailMsg(HttpSession session, String title, String exceptionMessage){
        session.setAttribute(title, exceptionMessage);
    }


}
