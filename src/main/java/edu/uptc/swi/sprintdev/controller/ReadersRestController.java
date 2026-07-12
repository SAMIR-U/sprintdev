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

@RestController
@RequestMapping("/api")
public class ReadersRestController extends AbstractController{
    private final IUserService userService;
    
    public ReadersRestController(IUserService userService){
        this.userService = userService;
    }

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
