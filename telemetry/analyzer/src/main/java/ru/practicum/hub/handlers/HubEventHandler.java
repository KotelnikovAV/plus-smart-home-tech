package ru.practicum.hub.handlers;

import ru.yandex.practicum.kafka.telemetry.event.HubEventAvro;
import ru.yandex.practicum.kafka.telemetry.event.HubsEventTypeAvro;

public interface HubEventHandler {
    HubsEventTypeAvro getMessageType();

    void handle(HubEventAvro hubEvent);
}
