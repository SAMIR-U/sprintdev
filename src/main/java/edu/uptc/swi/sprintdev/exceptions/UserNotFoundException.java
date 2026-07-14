package edu.uptc.swi.sprintdev.exceptions;

/**
 * Exception thrown when a requested user cannot be found in the system.
 */
public class UserNotFoundException extends RuntimeException {
    public UserNotFoundException(String message) {
        super(message);
    }
}
