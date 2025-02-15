package ru.practicum.events.handlers.sensor;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import ru.practicum.events.model.sensor.ClimateSensorEvent;
import ru.practicum.events.model.sensor.SensorEvent;
import ru.practicum.events.model.sensor.enums.SensorEventType;
import ru.practicum.events.producer.EventsProducer;
import ru.yandex.practicum.grpc.telemetry.event.ClimateSensorProto;
import ru.yandex.practicum.grpc.telemetry.event.SensorEventProto;
import ru.yandex.practicum.kafka.telemetry.event.ClimateSensorAvro;
import ru.yandex.practicum.kafka.telemetry.event.SensorEventAvro;
import ru.yandex.practicum.kafka.telemetry.event.SensorEventTypeAvro;

@Component
@RequiredArgsConstructor
public class ClimateSensorEventHandler extends SensorEventHandler {
    private final EventsProducer eventsProducer;

    @Override
    public SensorEventProto.PayloadCase getMessageTypeRPC() {
        return SensorEventProto.PayloadCase.CLIMATE_SENSOR;
    }

    @Override
    public SensorEventType getMessageTypeHTTP() {
        return SensorEventType.CLIMATE_SENSOR_EVENT;
    }

    @Override
    public void handle(SensorEventProto sensorEvent) {
        SensorEventAvro message = SensorEventAvro.newBuilder()
                .setHubId(sensorEvent.getHubId())
                .setId(sensorEvent.getId())
                .setTimestamp(getInstant(sensorEvent.getTimestamp()))
                .setPayload(getClimateSensorAvro(sensorEvent.getClimateSensor()))
                .setType(SensorEventTypeAvro.CLIMATE_SENSOR_EVENT)
                .build();
        eventsProducer.collectSensorEvent(message);
    }

    @Override
    public void handle(SensorEvent sensorEvent) {
        SensorEventAvro message = SensorEventAvro.newBuilder()
                .setHubId(sensorEvent.getHubId())
                .setId(sensorEvent.getId())
                .setTimestamp(sensorEvent.getTimestamp())
                .setPayload(getClimateSensorAvro((ClimateSensorEvent) sensorEvent))
                .setType(SensorEventTypeAvro.CLIMATE_SENSOR_EVENT)
                .build();
        eventsProducer.collectSensorEvent(message);
    }

    private ClimateSensorAvro getClimateSensorAvro(ClimateSensorProto climateSensorProto) {
        return ClimateSensorAvro.newBuilder()
                .setTemperatureC(climateSensorProto.getTemperatureC())
                .setHumidity(climateSensorProto.getHumidity())
                .setCo2Level(climateSensorProto.getCo2Level())
                .build();
    }

    private ClimateSensorAvro getClimateSensorAvro(ClimateSensorEvent climateSensorEvent) {
        return ClimateSensorAvro.newBuilder()
                .setTemperatureC(climateSensorEvent.getTemperatureC())
                .setHumidity(climateSensorEvent.getHumidity())
                .setCo2Level(climateSensorEvent.getCo2Level())
                .build();
    }
}
