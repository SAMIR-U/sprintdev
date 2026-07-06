package edu.uptc.swi.sprintdev.exceptions;

public class UserNameAlreadyExist extends RuntimeException {
    public UserNameAlreadyExist(String message) {
        super(message);
    }
}
