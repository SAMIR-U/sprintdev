package edu.uptc.swi.sprintdev.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import edu.uptc.swi.sprintdev.domain.User;
import edu.uptc.swi.sprintdev.service.interfaces.IUserService;
import jakarta.servlet.http.HttpSession;

/**
 * REST controller used to search for users that can be added as readers.
 * It validates authentication before returning the matching users.
 */
@RestController
@RequestMapping("/api")
public class ReadersRestController extends AbstractController {
    private final IUserService userService;

    /**
     * Creates a REST controller with the required user service.
     *
     * @param userService the service used to search users by keyword
     */
    public ReadersRestController(IUserService userService) {
        this.userService = userService;
    }

    /**
     * Searches for users whose username matches the provided keyword.
     *
     * @param key the search keyword used to find users
     * @param session the current HTTP session used to verify authentication
     * @return a 200 response with the list of matching users if the user is authenticated,
     *         or a 401 Unauthorized response if no authenticated user is present
     */
    @GetMapping("/findreaders")
    public ResponseEntity<List<User>> findReaders(@RequestParam String key,
                                                  HttpSession session) {
        User user = autenticatedUserIn(session);
        if (user == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        List<User> users = userService.findUserByKeyWord(key);
        return ResponseEntity.ok(users);
    }
}
