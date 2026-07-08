package edu.uptc.swi.sprintdev.controller.utils;

import edu.uptc.swi.sprintdev.domain.User;
import jakarta.servlet.http.HttpSession;

public final class SessionUtlis {
    public static User autenticatedUserIn(HttpSession session){
        User user = (User) session.getAttribute("user");
        return user;
    }
    public static void setAutenticatedUserIn(HttpSession session, User user){
        operSuccessMsg(session, "login");
        session.setAttribute("user", user);
    }
    public static void setSelectSprintIn(HttpSession session){
        session.setAttribute("user", "sprintselected");
    }
    public static void logout(HttpSession session){
        session.removeAttribute("user");
        session.removeAttribute("login");
    }
    public static void operSuccessMsg(HttpSession session, String title){
        session.setAttribute(title, "success");      
    }
    public static void operfailMsg(HttpSession session, String title){
        session.setAttribute(title, "fail");      
    }
    
} 
