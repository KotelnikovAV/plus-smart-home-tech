package ru.practicum.events.service.producer;

import ru.yandex.practicum.kafka.telemetry.event.SensorsSnapshotAvro;

public interface AggregatorProducerService {
    void aggregateSensorsSnapshot(SensorsSnapshotAvro message);
}
