package ru.practicum.sensor.handlers;

import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import ru.practicum.hub.model.Condition;
import ru.yandex.practicum.kafka.telemetry.event.SensorEventTypeAvro;
import ru.yandex.practicum.kafka.telemetry.event.SensorStateAvro;
import ru.yandex.practicum.kafka.telemetry.event.SwitchSensorAvro;

@Component
@NoArgsConstructor
@Slf4j
public class SwitchSensorHandler extends SnapshotHandler {

    @Override
    public SensorEventTypeAvro getMessageType() {
        return SensorEventTypeAvro.SWITCH_SENSOR_EVENT;
    }

    @Override
    public boolean handle(Condition condition, SensorStateAvro sensorStateAvro) {
        log.info("Handling switch sensor event");

        SwitchSensorAvro motionSensorAvro = (SwitchSensorAvro) sensorStateAvro.getData();
        if (motionSensorAvro.getState()) {
            return checkCondition(condition, 1);
        } else {
            return checkCondition(condition, 0);
        }
    }
}
