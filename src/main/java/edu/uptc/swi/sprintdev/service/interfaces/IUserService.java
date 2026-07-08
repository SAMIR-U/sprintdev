package edu.uptc.swi.sprintdev.service.interfaces;

import edu.uptc.swi.sprintdev.domain.User;

public interface IUserService {
    boolean registerUser(User user);
    boolean loginUser(String userName, String password);
    User obtainUserByUsername(String username);
}
