package edu.uptc.swi.sprintdev.service;

import edu.uptc.swi.sprintdev.domain.User;
import edu.uptc.swi.sprintdev.exceptions.UserNameAlreadyExistException;
import edu.uptc.swi.sprintdev.repository.IUserRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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
        throw new UserNameAlreadyExistException(user.getUserName());
    }

    @Override
    public boolean loginUser(String userName, String password) {
        User tempUser = this.userRepo.findByUsername(userName);
        if (tempUser != null) {
            return this.isCorrectPassword(tempUser, password);
        }
        return false;
    }

    @Override
    public User obtainUserByUsername(String username) {
        return this.userRepo.findByUsername(username);
    }

    private boolean userExist(User user) {
        return this.userRepo.findByUsername(user.getUserName()) != null;
    }

    private boolean isCorrectPassword(User user, String password) {
        return user.getPassword().equals(password);
    }
}
