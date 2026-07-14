package edu.uptc.swi.sprintdev.exceptions;

/**
 * Exception thrown when a requested task status transition is not allowed.
 */
public class StatusTaskIsNotPossibleToChangeException extends RuntimeException {
    public StatusTaskIsNotPossibleToChangeException(String message) {
        super(message);
    }
}
