package ru.practicum.events.hendlers.sensor;

import ru.yandex.practicum.grpc.telemetry.event.SensorEventProto;

import java.time.Instant;

public abstract class SensorEventHandler {

    public SensorEventProto.PayloadCase getMessageType() {
        return null;
    }

    public void handle(SensorEventProto sensorEvent) {

    }

    public Instant getInstant(com.google.protobuf.Timestamp timestamp) {
        return Instant.ofEpochSecond(timestamp.getSeconds(), timestamp.getNanos());
    }
}
