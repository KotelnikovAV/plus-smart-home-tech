package ru.practicum.events.handlers.sensor;

import ru.practicum.events.model.sensor.SensorEvent;
import ru.practicum.events.model.sensor.enums.SensorEventType;
import ru.yandex.practicum.grpc.telemetry.event.SensorEventProto;

import java.time.Instant;

public abstract class SensorEventHandler {

    public SensorEventProto.PayloadCase getMessageTypeRPC() {
        return null;
    }

    public SensorEventType getMessageTypeHTTP() {
        return null;
    }

    public void handle(SensorEventProto sensorEvent) {

    }

    public void handle(SensorEvent sensorEvent) {

    }

    public Instant getInstant(com.google.protobuf.Timestamp timestamp) {
        return Instant.ofEpochSecond(timestamp.getSeconds(), timestamp.getNanos());
    }
}
