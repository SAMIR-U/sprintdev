package edu.uptc.swi.sprintdev.domain;

/**
 * Represents the lifecycle states of a sprint.
 * This enum only defines possible values; transition rules are implemented
 * in {@code service.rules.SprintLifecycleRules}.
 */
public enum SprintStatus {
    CREATED,
    ACTIVE,
    CLOSED
}
