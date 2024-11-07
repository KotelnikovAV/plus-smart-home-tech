package ru.practicum.sensor.handlers;

import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import ru.practicum.hub.model.Condition;
import ru.yandex.practicum.kafka.telemetry.event.LightSensorAvro;
import ru.yandex.practicum.kafka.telemetry.event.SensorEventTypeAvro;
import ru.yandex.practicum.kafka.telemetry.event.SensorStateAvro;

@Component
@NoArgsConstructor
@Slf4j
public class LightSensorHandler extends SnapshotHandler {

    @Override
    public SensorEventTypeAvro getMessageType() {
        return SensorEventTypeAvro.LIGHT_SENSOR_EVENT;
    }

    @Override
    public boolean handle(Condition condition, SensorStateAvro sensorStateAvro) {
        log.info("Handling light sensor event");

        LightSensorAvro lightSensorAvro = (LightSensorAvro) sensorStateAvro.getData();
        return checkCondition(condition, lightSensorAvro.getLuminosity());
    }
}
