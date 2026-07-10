package edu.uptc.swi.sprintdev.exceptions;

public class UserAlreadyExistInListException extends RuntimeException {
    public UserAlreadyExistInListException(String message) {
        super(message);
    }
}
