package edu.uptc.swi.sprintdev.exceptions;

/**
 * Exception thrown when a sprint task list would become empty but at least one task is required.
 */
public class TheListNeedAtleastOneTaskException extends RuntimeException {
    public TheListNeedAtleastOneTaskException(String message) {
        super(message);
    }
}
