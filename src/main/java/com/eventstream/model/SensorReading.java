package com.eventstream.model;

import java.util.Objects;

/**
 * A numeric measurement from a sensor, for example:
 *
 * <pre>
 * ts=2024-05-10T08:00:00Z sensor=temperature_celsius device=thermostat-01 reading=21.5
 * </pre>
 *
 * <p>The sensor aggregator groups these by {@link #sensor()}.
 *
 * @param sensor  the sensor name the measurement is grouped by; never blank
 * @param reading the measured value; always finite
 */
public record SensorReading(String sensor, double reading) implements Record {

    public SensorReading {
        Objects.requireNonNull(sensor, "sensor");
        if (sensor.isBlank()) {
            throw new IllegalArgumentException("sensor must not be blank");
        }
        if (!Double.isFinite(reading)) {
            throw new IllegalArgumentException("reading must be finite but was " + reading);
        }
    }
}
