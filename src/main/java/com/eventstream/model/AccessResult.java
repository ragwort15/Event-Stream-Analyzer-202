package com.eventstream.model;

import java.util.Locale;
import java.util.Optional;

/** Outcome of a door access attempt. */
public enum AccessResult {
    GRANTED,
    DENIED;

    /**
     * Resolves a raw {@code result} field value, ignoring case and surrounding spaces.
     *
     * @param token the raw field value, possibly null, blank or unrecognised
     * @return the matching result, or empty if the token names none
     */
    public static Optional<AccessResult> from(String token) {
        if (token == null || token.isBlank()) {
            return Optional.empty();
        }
        try {
            return Optional.of(valueOf(token.strip().toUpperCase(Locale.ROOT)));
        } catch (IllegalArgumentException unknownResult) {
            return Optional.empty();
        }
    }
}
