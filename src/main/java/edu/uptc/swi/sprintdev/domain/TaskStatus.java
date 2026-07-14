package edu.uptc.swi.sprintdev.domain;

import java.util.EnumSet;
import java.util.Set;

/**
 * Defines the possible statuses for a task and the allowed transitions
 * between those statuses.
 */
public enum TaskStatus {
    /**
     * Task has been created but work has not started yet.
     */
    PENDING {
        @Override
        public Set<TaskStatus> allowedTransition() {
            return EnumSet.of(IN_PROGRESS);
        }
    },
    /**
     * Task work is currently in progress.
     */
    IN_PROGRESS {
        @Override
        public Set<TaskStatus> allowedTransition() {
            return EnumSet.of(IN_REVIEW);
        }
    },
    /**
     * Task is under review and may either return to in progress or complete.
     */
    IN_REVIEW {
        @Override
        public Set<TaskStatus> allowedTransition() {
            return EnumSet.of(IN_PROGRESS, COMPLETED);
        }
    },
    /**
     * Task has been completed and cannot transition to any other state.
     */
    COMPLETED {
        @Override
        public Set<TaskStatus> allowedTransition() {
            return EnumSet.noneOf(TaskStatus.class);
        }
    };

    /**
     * Returns the set of statuses that this status may transition to.
     *
     * @return allowed next statuses from this status
     */
    public abstract Set<TaskStatus> allowedTransition();

    /**
     * Checks whether a transition from this status to the provided status is allowed.
     *
     * @param nextStatus the target status to validate
     * @return true if the transition is permitted, false otherwise
     */
    public boolean mayAllowedTransition(TaskStatus nextStatus) {
        return allowedTransition().contains(nextStatus);
    }
    }
