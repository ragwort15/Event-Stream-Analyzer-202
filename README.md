# Event-Stream-Analyzer-202

## About the project

Event Stream Analyzer is a Java command-line application that turns raw IoT / smart-building event logs into
summary reports.

Smart buildings produce a constant stream of log lines from sensors, devices and door access systems. Each line
is a set of `key=value` pairs, for example:

```
ts=2024-05-10T08:00:00Z sensor=temperature_celsius device=thermostat-01 reading=21.5
ts=2024-05-10T08:00:05Z severity=INFO device=thermostat-01 msg="Heartbeat OK"
ts=2024-05-10T08:00:10Z user=user-42 door=door-A result=DENIED duration_ms=30
```

The application reads a file of these lines and does four things:

1. **Parses** each line into key-value pairs, including quoted values that contain spaces.
2. **Classifies** each line as one of three record types, based on which keys it contains.
3. **Aggregates** statistics for each record type.
4. **Writes** the results to one JSON file per record type.

Corrupted lines, lines with missing keys and lines with invalid values are skipped silently, and processing
continues.

## Record types and outputs

| Record type | Identified by | Summary produced | Output file |
|---|---|---|---|
| **Sensor Readings** | `sensor` + numeric `reading` | Per sensor: count, minimum, median, average, maximum, standard deviation | `sensors.json` |
| **Device Events** | `severity` (CRITICAL / ERROR / WARN / INFO) + `device` | Event count per severity; per device, the total and the most frequent severity | `devices.json` |
| **Access Records** | `user`, `door`, `result` (GRANTED / DENIED), `duration_ms` | Per door: decision-time min, max and 50th/90th/99th percentiles; GRANTED vs DENIED counts; users with 3+ denied attempts (security flag) | `access.json` |

## Design goal

The application is built around design patterns so it is easy to extend. You can add a new record type (e.g.
network logs), a new input format (e.g. JSON Lines) or a new output format (e.g. CSV) by adding new classes,
without changing the existing ones.

## Project structure

```text
event-stream-analyzer/
├── pom.xml                         # Maven build (Java 17, Gson, JUnit 5, shade plugin)
├── README.md                       # Build, run, and design notes
├── .gitignore                      # Ignores target/
├── docs/
│   └── design.pdf                  # Part I: problem, patterns, consequences, UML, extensibility
├── samples/
│   └── events.txt                  # Sample input from the assignment
└── src/
    ├── main/java/com/eventstream/
    │   ├── Main.java               # Entry point: CLI errors and exit codes
    │   ├── engine/
    │   │   ├── EventStreamAnalyzer.java   # Runs the pipeline: parse → classify → aggregate → write
    │   │   └── CliArgs.java               # Parses and validates --file and --outdir
    │   ├── parse/                  # Strategy: input formats
    │   │   ├── InputParser.java
    │   │   └── KeyValueParser.java
    │   ├── model/                  # Typed records
    │   │   ├── Record.java                # Sealed interface
    │   │   ├── SensorReading.java
    │   │   ├── DeviceEvent.java
    │   │   ├── AccessRecord.java
    │   │   ├── Severity.java
    │   │   └── AccessResult.java
    │   ├── process/                # Chain of Responsibility + Template Method: classification
    │   │   ├── RecordHandler.java
    │   │   ├── SensorReadingHandler.java
    │   │   ├── DeviceEventHandler.java
    │   │   ├── AccessRecordHandler.java
    │   │   └── UnrecognizedLineHandler.java
    │   ├── stats/                  # Strategy: per-type aggregation
    │   │   ├── Aggregator.java
    │   │   ├── SensorAggregator.java
    │   │   ├── DeviceEventAggregator.java
    │   │   ├── AccessRecordAggregator.java
    │   │   └── StatsUtil.java             # median, mean, std dev, nearest-rank percentile, rounding
    │   └── output/                 # Strategy: output formats
    │       ├── OutputWriter.java
    │       └── JsonOutputWriter.java
    └── test/java/com/eventstream/
        ├── engine/                 # EventStreamAnalyzerTest, CliArgsTest
        ├── parse/                  # KeyValueParserTest
        ├── process/                # HandlerChainTest
        ├── stats/                  # StatsUtilTest, one test class per aggregator
        └── output/                 # JsonOutputWriterTest
```


    
   
## Tech stack

- Java 17
- Maven
- Gson (JSON output)
- JUnit 5 (tests)
