package edu.uptc.swi.sprintdev.exceptions;

/**
 * Exception thrown when an operation requires an active sprint but the sprint is not active.
 */
public class SprintIsNotActiveException extends RuntimeException {
    public SprintIsNotActiveException(String message) {
        super(message);
    }
}
