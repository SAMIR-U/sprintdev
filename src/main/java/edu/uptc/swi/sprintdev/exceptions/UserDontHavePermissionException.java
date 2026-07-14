package edu.uptc.swi.sprintdev.exceptions;

/**
 * Exception thrown when a user attempts an operation they are not permitted to perform.
 */
public class UserDontHavePermissionException extends RuntimeException {
    public UserDontHavePermissionException(String message) {
        super(message);
    }
}
