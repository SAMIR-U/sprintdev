package edu.uptc.swi.sprintdev.service.implement;

import edu.uptc.swi.sprintdev.domain.User;
import edu.uptc.swi.sprintdev.exceptions.UserNotFoundException;
import edu.uptc.swi.sprintdev.repository.IUserRepo;
import edu.uptc.swi.sprintdev.service.interfaces.IUserService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

/**
 * Implementation of user management services.
 * Handles user registration, login authentication, and user lookup operations.
 */
@Service
@Transactional
public class UserServiceImpl implements IUserService {
    private final IUserRepo userRepo;

    public UserServiceImpl(IUserRepo userRepo) {
        this.userRepo = userRepo;
    }

    @Override
    public boolean registerUser(User user) {
        if (!this.userExist(user)) {
            this.userRepo.save(user);
            return true;
        }
        return false;
    }

    @Override
    public boolean loginUser(String userName, String password) {
        Optional<User> tempUser = this.userRepo.findByUsername(userName);
        return tempUser.filter(user -> this.isCorrectPassword(user, password)).isPresent();
    }

    @Override
    public User obtainUserByUsername(String username) {
        return this.userRepo.findByUsername(username)
                .orElseThrow(() -> new UserNotFoundException("Usuario no encontrado: " + username));
    }

    @Override
    public List<User> findUserByKeyWord(String keyword) {
        return this.userRepo.findUserByKeyWord(keyword);
    }

    /**
     * Check whether a user already exists by username.
     *
     * @param user the user to check
     * @return true when a user with the same username exists
     */
    private boolean userExist(User user) {
        return this.userRepo.findByUsername(user.getUserName()).isPresent();
    }

    /**
     * Validate the provided password against the stored user password.
     *
     * @param user the user whose password is checked
     * @param password the password provided during login
     * @return true when the provided password matches the stored password
     */
    private boolean isCorrectPassword(User user, String password) {
        return user.getPassword().equals(password);
    }
}