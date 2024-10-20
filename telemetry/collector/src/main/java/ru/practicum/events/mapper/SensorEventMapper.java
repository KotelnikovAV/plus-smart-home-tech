package ru.practicum.events.mapper;

import lombok.experimental.UtilityClass;
import org.apache.avro.specific.SpecificRecordBase;
import ru.practicum.events.model.sensor.*;
import ru.yandex.practicum.kafka.telemetry.event.*;

@UtilityClass
public class SensorEventMapper {
    public SpecificRecordBase getSensorEventAvro(SensorEvent sensorEvent) {
        switch (sensorEvent.getType()) {
            case LIGHT_SENSOR_EVENT -> {
                return LightSensorAvro.newBuilder()
                        .setLinkQuality(((LightSensorEvent) sensorEvent).getLinkQuality())
                        .setLuminosity(((LightSensorEvent) sensorEvent).getLuminosity())
                        .build();
            }
            case MOTION_SENSOR_EVENT -> {
                return MotionSensorAvro.newBuilder()
                        .setLinkQuality(((MotionSensorEvent) sensorEvent).getLinkQuality())
                        .setMotion(((MotionSensorEvent) sensorEvent).getMotion())
                        .setVoltage(((MotionSensorEvent) sensorEvent).getVoltage())
                        .build();
            }
            case SWITCH_SENSOR_EVENT -> {
                return SwitchSensorAvro.newBuilder()
                        .setState(((SwitchSensorEvent) sensorEvent).getState())
                        .build();
            }
            case CLIMATE_SENSOR_EVENT -> {
                return ClimateSensorAvro.newBuilder()
                        .setCo2Level(((ClimateSensorEvent) sensorEvent).getCo2Level())
                        .setHumidity(((ClimateSensorEvent) sensorEvent).getHumidity())
                        .setTemperatureC(((ClimateSensorEvent) sensorEvent).getTemperatureC())
                        .build();
            }
            case TEMPERATURE_SENSOR_EVENT -> {
                return TemperatureSensorAvro.newBuilder()
                        .setTemperatureC(((TemperatureSensorEvent) sensorEvent).getTemperatureC())
                        .setTemperatureF(((TemperatureSensorEvent) sensorEvent).getTemperatureF())
                        .build();
            }
        }
        return null;
    }
}
