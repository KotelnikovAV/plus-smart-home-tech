package ru.practicum.events.service;

import ru.yandex.practicum.kafka.telemetry.event.HubEventAvro;
import ru.yandex.practicum.kafka.telemetry.event.SensorEventAvro;

public interface EventsService {
    void collectSensorEvent(SensorEventAvro message);

    void collectHubEvent(HubEventAvro message);
}
