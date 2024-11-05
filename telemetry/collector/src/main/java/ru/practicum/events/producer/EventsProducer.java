package ru.practicum.events.producer;

import ru.yandex.practicum.kafka.telemetry.event.HubEventAvro;
import ru.yandex.practicum.kafka.telemetry.event.SensorEventAvro;

public interface EventsProducer {
    void collectSensorEvent(SensorEventAvro message);

    void collectHubEvent(HubEventAvro message);
}
