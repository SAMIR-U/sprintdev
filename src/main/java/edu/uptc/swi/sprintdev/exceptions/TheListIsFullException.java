package edu.uptc.swi.sprintdev.exceptions;

/**
 * Exception thrown when attempting to add an item to a list that has reached its allowed capacity.
 */
public class TheListIsFullException extends RuntimeException {
    public TheListIsFullException(String message) {
        super(message);
    }
}
