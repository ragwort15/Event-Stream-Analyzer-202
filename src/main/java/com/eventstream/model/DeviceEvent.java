package com.eventstream.model;

import java.util.Objects;

/**
 * A severity-tagged event reported by a device, for example:
 *
 * <pre>
 * ts=2024-05-10T08:00:45Z severity=CRITICAL device=boiler-03 msg="Pressure out of range"
 * </pre>
 *
 * @param device   the device that reported the event; never blank
 * @param severity how serious the event is; never null
 */
public record DeviceEvent(String device, Severity severity) implements Record {

    public DeviceEvent {
        Objects.requireNonNull(device, "device");
        Objects.requireNonNull(severity, "severity");
        if (device.isBlank()) {
            throw new IllegalArgumentException("device must not be blank");
        }
    }
}
