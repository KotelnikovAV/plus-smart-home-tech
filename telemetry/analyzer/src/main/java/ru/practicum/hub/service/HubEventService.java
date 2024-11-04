package ru.practicum.hub.service;

import ru.yandex.practicum.kafka.telemetry.event.HubEventAvro;

public interface HubEventService {
    void handleEvent(HubEventAvro event);
}
