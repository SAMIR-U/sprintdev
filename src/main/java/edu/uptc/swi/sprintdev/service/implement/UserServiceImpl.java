package edu.uptc.swi.sprintdev.service.implement;

import edu.uptc.swi.sprintdev.domain.User;
import edu.uptc.swi.sprintdev.exceptions.UserNotFoundException;
import edu.uptc.swi.sprintdev.repository.IUserRepo;
import edu.uptc.swi.sprintdev.service.interfaces.IUserService;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
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

    private boolean userExist(User user) {
        return this.userRepo.findByUsername(user.getUserName()).isPresent();
    }

    private boolean isCorrectPassword(User user, String password) {
        return user.getPassword().equals(password);
    }
}