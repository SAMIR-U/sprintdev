package edu.uptc.swi.sprintdev.domain;

/**
 * Columnas del tablero Scrum. Solo enumera los valores posibles; la
 * regla de qué transición entre columnas es válida está en
 * {@code service.rules.TaskBoardRules}, no aquí.
 */
public enum TaskStatus {
    PENDING,
    IN_PROGRESS,
    IN_REVIEW,
    COMPLETED
}
