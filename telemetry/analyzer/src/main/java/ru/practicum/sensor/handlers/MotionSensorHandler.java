package ru.practicum.sensor.handlers;

import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import ru.practicum.hub.model.Condition;
import ru.yandex.practicum.kafka.telemetry.event.MotionSensorAvro;
import ru.yandex.practicum.kafka.telemetry.event.SensorEventTypeAvro;
import ru.yandex.practicum.kafka.telemetry.event.SensorStateAvro;

@Component
@NoArgsConstructor
@Slf4j
public class MotionSensorHandler extends SnapshotHandler {

    @Override
    public SensorEventTypeAvro getMessageType() {
        return SensorEventTypeAvro.MOTION_SENSOR_EVENT;
    }

    @Override
    public boolean handle(Condition condition, SensorStateAvro sensorStateAvro) {
        log.info("Handling motion sensor event");

        MotionSensorAvro motionSensorAvro = (MotionSensorAvro) sensorStateAvro.getData();
        if (motionSensorAvro.getMotion()) {
            return checkCondition(condition, 1);
        } else {
            return checkCondition(condition, 0);
        }
    }
}
