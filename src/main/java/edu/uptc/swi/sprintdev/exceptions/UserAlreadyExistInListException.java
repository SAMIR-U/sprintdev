package edu.uptc.swi.sprintdev.exceptions;

/**
 * Exception thrown when a user is already present in the requested list.
 */
public class UserAlreadyExistInListException extends RuntimeException {
    public UserAlreadyExistInListException(String message) {
        super(message);
    }
}
