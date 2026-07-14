package edu.uptc.swi.sprintdev.exceptions;

/**
 * Exception thrown when an operation is attempted on a sprint that has already been closed.
 */
public class SprintIsClosedException extends RuntimeException {
    public SprintIsClosedException(String message) {
        super(message);
    }
}
