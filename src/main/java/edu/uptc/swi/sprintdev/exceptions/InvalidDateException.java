package edu.uptc.swi.sprintdev.exceptions;

/**
 * Exception thrown when a sprint date input is invalid.
 * Examples include end dates before start dates or malformed date values.
 */
public class InvalidDateException extends RuntimeException {
    public InvalidDateException(String message) {
        super(message);
    }
}
