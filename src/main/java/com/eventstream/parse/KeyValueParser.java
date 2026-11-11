package com.eventstream.parse;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Reads whitespace-separated {@code key=value} pairs, for example:
 *
 * <pre>
 * ts=2024-05-10T08:00:05Z severity=INFO device=thermostat-01 msg="Heartbeat OK"
 * </pre>
 *
 * <p>Parsing is deliberately lenient, because a log file is allowed to contain
 * corrupted lines and processing must continue:
 *
 * <ul>
 *   <li>A value may be wrapped in double quotes to hold spaces. The quotes are
 *       removed, and inside them {@code \"} is a literal quote and {@code \\} a
 *       literal backslash.</li>
 *   <li>A token with no {@code =} is junk and is skipped; the valid pairs on the same
 *       line are still returned.</li>
 *   <li>A token with an empty key is skipped.</li>
 *   <li>An unterminated quote runs to the end of the line rather than throwing.</li>
 *   <li>A repeated key keeps its last value.</li>
 * </ul>
 *
 * <p>Keys come back in the order encountered. This class holds no state, so one
 * instance can be shared.
 */
public final class KeyValueParser implements InputParser {

    private static final char PAIR_SEPARATOR = '=';
    private static final char QUOTE = '"';
    private static final char ESCAPE = '\\';

    @Override
    public Map<String, String> parse(String line) {
        Map<String, String> fields = new LinkedHashMap<>();
        if (line == null || line.isBlank()) {
            return fields;
        }

        int i = 0;
        final int n = line.length();

        while (i < n) {
            // Skip the whitespace between tokens.
            while (i < n && Character.isWhitespace(line.charAt(i))) {
                i++;
            }
            if (i >= n) {
                break;
            }

            // Read the key, stopping at '=' or at the end of the token.
            final int keyStart = i;
            while (i < n && line.charAt(i) != PAIR_SEPARATOR && !Character.isWhitespace(line.charAt(i))) {
                i++;
            }
            if (i >= n || line.charAt(i) != PAIR_SEPARATOR) {
                continue;  // junk token with no '=' - skip it and read the next one
            }
            final String key = line.substring(keyStart, i);
            i++;  // consume the '='

            // Read the value, quoted or bare.
            final String value;
            if (i < n && line.charAt(i) == QUOTE) {
                i++;  // consume the opening quote
                StringBuilder sb = new StringBuilder();
                while (i < n) {
                    char c = line.charAt(i);
                    if (c == ESCAPE && i + 1 < n) {
                        char next = line.charAt(i + 1);
                        if (next == QUOTE || next == ESCAPE) {
                            sb.append(next);
                            i += 2;
                            continue;
                        }
                    }
                    if (c == QUOTE) {
                        i++;  // consume the closing quote
                        break;
                    }
                    sb.append(c);
                    i++;
                }
                value = sb.toString();  // an unterminated quote simply ends with the line
            } else {
                final int valueStart = i;
                while (i < n && !Character.isWhitespace(line.charAt(i))) {
                    i++;
                }
                value = line.substring(valueStart, i);
            }

            if (!key.isEmpty()) {
                fields.put(key, value);
            }
        }

        return fields;
    }
}
