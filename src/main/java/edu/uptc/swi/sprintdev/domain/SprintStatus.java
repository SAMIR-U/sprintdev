package edu.uptc.swi.sprintdev.domain;

/**
 * Ciclo de vida de un Sprint. Solo enumera los valores posibles; las
 * reglas de qué transición es válida en cada momento están en
 * {@code service.rules.SprintLifecycleRules}, no aquí.
 */
public enum SprintStatus {
    CREATED,
    ACTIVE,
    CLOSED
}
