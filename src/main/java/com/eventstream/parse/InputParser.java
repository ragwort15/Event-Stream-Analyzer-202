package com.eventstream.parse;

import java.util.Map;

/**
 * Turns one raw input line into its field values.
 *
 * <p>Strategy: the rest of the pipeline depends only on this interface, so a new input
 * format (JSON Lines, CSV, ...) is added by writing another implementation rather than
 * by changing any existing class.
 */
public interface InputParser {

    /**
     * Parses a single line into its key-value fields.
     *
     * <p>Implementations never throw. A line that is null, blank or carries nothing
     * usable yields an empty map, which the handler chain then declines to classify.
     *
     * @param line the raw line, possibly null, blank or malformed
     * @return the fields found, in the order they appeared; never null
     */
    Map<String, String> parse(String line);
}
