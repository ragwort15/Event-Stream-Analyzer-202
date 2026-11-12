package com.eventstream.model;

import java.util.Objects;

/**
 * One door access attempt and how long the decision took, for example:
 *
 * <pre>
 * ts=2024-05-10T08:00:10Z user=user-42 door=door-A result=DENIED duration_ms=30
 * </pre>
 *
 * @param user       the user who tried the door; never blank
 * @param door       the door that was tried; never blank
 * @param result     whether access was granted or denied; never null
 * @param durationMs how long the decision took, in whole milliseconds; never negative
 */
public record AccessRecord(String user, String door, AccessResult result, int durationMs)
        implements Record {

    public AccessRecord {
        Objects.requireNonNull(user, "user");
        Objects.requireNonNull(door, "door");
        Objects.requireNonNull(result, "result");
        if (user.isBlank()) {
            throw new IllegalArgumentException("user must not be blank");
        }
        if (door.isBlank()) {
            throw new IllegalArgumentException("door must not be blank");
        }
        if (durationMs < 0) {
            throw new IllegalArgumentException("durationMs must not be negative but was " + durationMs);
        }
    }

    /**
     * Whether this attempt was denied, the condition the security flag counts.
     *
     * @return true if the result was {@link AccessResult#DENIED}
     */
    public boolean denied() {
        return result == AccessResult.DENIED;
    }
}
