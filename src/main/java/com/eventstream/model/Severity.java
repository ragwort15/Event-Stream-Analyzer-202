package com.eventstream.model;

import java.util.Locale;
import java.util.Optional;

/**
 * Severity of a device event, from most to least serious.
 *
 * <p>The declaration order is the severity order, so {@link #compareTo} and
 * {@code values()} both run CRITICAL first. Anything outside this set, such as the
 * {@code severity=LOW} line in the sample log, is not a device event and its line is
 * skipped.
 */
public enum Severity {
    CRITICAL,
    ERROR,
    WARN,
    INFO;

    /**
     * Resolves a raw {@code severity} field value, ignoring case and surrounding spaces.
     *
     * @param token the raw field value, possibly null, blank or unrecognised
     * @return the matching severity, or empty if the token names none
     */
    public static Optional<Severity> from(String token) {
        if (token == null || token.isBlank()) {
            return Optional.empty();
        }
        try {
            return Optional.of(valueOf(token.strip().toUpperCase(Locale.ROOT)));
        } catch (IllegalArgumentException unknownSeverity) {
            return Optional.empty();
        }
    }
}
