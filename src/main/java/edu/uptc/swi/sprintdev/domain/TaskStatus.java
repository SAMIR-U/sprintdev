package edu.uptc.swi.sprintdev.domain;

import java.util.EnumSet;
import java.util.Set;

public enum TaskStatus {
    PENDING {
        @Override
        public Set<TaskStatus> allowedTransition() {
            return EnumSet.of(IN_PROGRESS);
        }
    },
    IN_PROGRESS {
        @Override
        public Set<TaskStatus> allowedTransition() {
            return EnumSet.of(IN_REVIEW);
        }
    },
    IN_REVIEW {
        @Override
        public Set<TaskStatus> allowedTransition() {
            return EnumSet.of(IN_PROGRESS, COMPLETED);
        }
    },
    COMPLETED {
        @Override
        public Set<TaskStatus> allowedTransition() {
            return EnumSet.noneOf(TaskStatus.class);
        }
    };

    public abstract Set<TaskStatus> allowedTransition();

    public boolean mayAllowedTransition(TaskStatus nextStatus) {
        return allowedTransition().contains(nextStatus);
    }
    }
