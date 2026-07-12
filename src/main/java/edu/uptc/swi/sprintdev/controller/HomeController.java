package edu.uptc.swi.sprintdev.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

/**
 * Controller responsible for handling requests to the application's
 * home page.
 */
@Controller
public class HomeController {

    /**
     * Returns the application's landing page.
     *
     * @return the view name of the home page
     */
    @GetMapping("/")
    public String getMethodName() {
        return "index";
    }
}
