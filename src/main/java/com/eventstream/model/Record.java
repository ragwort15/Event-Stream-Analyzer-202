package com.eventstream.model;

/**
 * Common type for every classified record.
 *
 * <p>Sealed over exactly the three record types the analyzer understands, so the
 * aggregators can switch on a record and the compiler can prove no case is missing.
 *
 * <p>A fourth record type (network logs, say) is added by naming it in the
 * {@code permits} clause below and adding its handler and aggregator; no existing
 * handler or aggregator changes.
 *
 * <p>Note: this interface, not {@link java.lang.Record}, is what the pipeline means by
 * "record". Classes outside this package must import it explicitly.
 */
public sealed interface Record permits SensorReading, DeviceEvent, AccessRecord {
}
