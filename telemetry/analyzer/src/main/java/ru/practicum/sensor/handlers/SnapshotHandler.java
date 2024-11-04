package ru.practicum.sensor.handlers;

import lombok.extern.slf4j.Slf4j;
import ru.practicum.hub.model.Condition;
import ru.yandex.practicum.kafka.telemetry.event.SensorEventTypeAvro;
import ru.yandex.practicum.kafka.telemetry.event.SensorStateAvro;

@Slf4j
public abstract class SnapshotHandler {
    public abstract SensorEventTypeAvro getMessageType();

    public abstract boolean handle(Condition condition, SensorStateAvro sensorStateAvro);

    protected boolean checkCondition(Condition condition, Integer value) {
        log.info("Checking condition {} and value {} ", condition, value);

        switch (condition.getOperation()) {
            case LOWER_THAN -> {
                return condition.getAmount() > value;
            }
            case GREATER_THAN -> {
                return condition.getAmount() < value;
            }
            case EQUALS -> {
                return condition.getAmount().equals(value);
            }
        }
        return false;
    }
}