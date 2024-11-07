package ru.practicum.events.handlers.hub;

import ru.practicum.events.model.hub.HubEvent;
import ru.practicum.events.model.hub.enums.HubsEventType;
import ru.yandex.practicum.grpc.telemetry.event.HubEventProto;

import java.time.Instant;

public abstract class HubEventHandler {

    public HubEventProto.PayloadCase getMessageTypeRPC() {
        return null;
    }

    public HubsEventType getMessageTypeHTTP() {
        return null;
    }

    public void handle(HubEventProto hubEvent) {

    }

    public void handle(HubEvent hubEvent) {

    }

    public Instant getInstant(com.google.protobuf.Timestamp timestamp) {
        return Instant.ofEpochSecond(timestamp.getSeconds(), timestamp.getNanos());
    }
}
