package ru.practicum.sensor.handlers;

import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import ru.practicum.hub.model.Condition;
import ru.yandex.practicum.kafka.telemetry.event.SensorEventTypeAvro;
import ru.yandex.practicum.kafka.telemetry.event.SensorStateAvro;
import ru.yandex.practicum.kafka.telemetry.event.TemperatureSensorAvro;

@Component
@NoArgsConstructor
@Slf4j
public class TemperatureSensorHandler extends SnapshotHandler {

    @Override
    public SensorEventTypeAvro getMessageType() {
        return SensorEventTypeAvro.TEMPERATURE_SENSOR_EVENT;
    }

    @Override
    public boolean handle(Condition condition, SensorStateAvro sensorStateAvro) {
        log.info("Handling temperature sensor event");
        TemperatureSensorAvro temperatureSensorAvro = (TemperatureSensorAvro) sensorStateAvro.getData();
        return checkCondition(condition, temperatureSensorAvro.getTemperatureC());
    }
}
