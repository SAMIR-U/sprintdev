package edu.uptc.swi.sprintdev.service.interfaces;

import edu.uptc.swi.sprintdev.domain.User;

import java.util.List;

/**
 * Service contract for user-related operations.
 *
 * Provides methods to register users, authenticate logins, and search for users
 * by username or keyword.
 */
public interface IUserService {
    /**
     * Register a new user.
     *
     * @param user the user to create
     * @return true if the registration succeeds
     */
    boolean registerUser(User user);

    /**
     * Authenticate a user using username and password.
     *
     * @param userName the username to authenticate
     * @param password the password to validate
     * @return true if the credentials are valid
     */
    boolean loginUser(String userName, String password);

    /**
     * Retrieve a user by username.
     *
     * @param username the username to search for
     * @return the matching user, or null if not found
     */
    User obtainUserByUsername(String username);

    /**
     * Search users by a keyword related to username or profile fields.
     *
     * @param keyword the search keyword
     * @return list of users matching the keyword
     */
    List<User> findUserByKeyWord(String keyword);
}
