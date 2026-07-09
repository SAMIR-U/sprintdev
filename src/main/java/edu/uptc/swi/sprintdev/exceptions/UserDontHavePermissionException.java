package edu.uptc.swi.sprintdev.exceptions;

public class UserDontHavePermissionException extends RuntimeException {
    public UserDontHavePermissionException(String message) {
        super(message);
    }
}
