package ru.practicum.sensor.handlers;

import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import ru.practicum.hub.model.Condition;
import ru.yandex.practicum.kafka.telemetry.event.ClimateSensorAvro;
import ru.yandex.practicum.kafka.telemetry.event.SensorEventTypeAvro;
import ru.yandex.practicum.kafka.telemetry.event.SensorStateAvro;

@Component
@NoArgsConstructor
@Slf4j
public class ClimateSensorHandler extends SnapshotHandler {

    @Override
    public SensorEventTypeAvro getMessageType() {
        return SensorEventTypeAvro.CLIMATE_SENSOR_EVENT;
    }

    @Override
    public boolean handle(Condition condition, SensorStateAvro sensorStateAvro) {
        log.info("Handling climate sensor event");

        ClimateSensorAvro climateSensorAvro = (ClimateSensorAvro) sensorStateAvro.getData();
        switch (condition.getType()) {
            case TEMPERATURE -> {
                return checkCondition(condition, climateSensorAvro.getTemperatureC());
            }
            case HUMIDITY -> {
                return checkCondition(condition, climateSensorAvro.getHumidity());
            }
            case CO2LEVEL ->
            {
                return checkCondition(condition, climateSensorAvro.getCo2Level());
            }
        }
        return false;
    }
}
