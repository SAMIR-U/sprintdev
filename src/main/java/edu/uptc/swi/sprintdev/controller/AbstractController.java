package edu.uptc.swi.sprintdev.controller;

import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import edu.uptc.swi.sprintdev.domain.User;
import jakarta.servlet.http.HttpSession;

/**
 * Provides common utility methods for controllers, including
 * authenticated user management and flash message handling.
 */
public abstract class AbstractController {

    /**
     * Retrieves the authenticated user stored in the current HTTP session.
     *
     * @param session the current HttpSession where the authenticated user is stored
     * @return the User object stored under the "user" attribute, or null if no user is logged in
     */
    protected User autenticatedUserIn(HttpSession session) {
        User user = (User) session.getAttribute("user");
        return user;
    }

    /**
     * Stores the authenticated user in the current session and adds a login success
     * flash attribute for the next request.
     *
     * @param session the current HttpSession used to save the authenticated user
     * @param redirect object used to add flash attributes for the next request
     * @param user the user to be stored in the session after a successful login
     */
    protected void setAutenticatedUserIn(HttpSession session, RedirectAttributes redirect, User user) {
        operSuccessMsg(redirect, "login");
        session.setAttribute("user", user);
    }

    /**
     * Adds a success status as a flash attribute for the next request.
     *
     * @param redirect object used to add flash attributes for the next request
     * @param title the attribute name under which the success message will be stored
     */
    protected void operSuccessMsg(RedirectAttributes redirect, String title) {
        redirect.addFlashAttribute(title, "success");
    }

    /**
     * Adds a failure status as a flash attribute for the next request.
     *
     * @param redirect object used to add flash attributes for the next request
     * @param title the attribute name under which the failure message will be stored
     */
    protected void operfailMsg(RedirectAttributes redirect, String title) {
        redirect.addFlashAttribute(title, "fail");
    }

    /**
     * Adds a custom error message as a flash attribute.
     *
     * @param redirect object used to add flash attributes for the next request
     * @param title the attribute name under which the message will be stored
     * @param exceptionMessage the detailed message to be shown to the user
     */
    protected void operfailMsg(RedirectAttributes redirect, String title, String exceptionMessage) {
        redirect.addFlashAttribute(title, exceptionMessage);
    }
}
