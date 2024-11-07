package ru.practicum.sensor.service;

import ru.yandex.practicum.kafka.telemetry.event.SensorsSnapshotAvro;

public interface AnalyzerSnapshotService {
    void handleSnapshot(SensorsSnapshotAvro snapshot);
}
