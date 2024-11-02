package ru.practicum.events.handlers.sensor;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import ru.practicum.events.model.sensor.SensorEvent;
import ru.practicum.events.model.sensor.TemperatureSensorEvent;
import ru.practicum.events.model.sensor.enums.SensorEventType;
import ru.practicum.events.service.EventsService;
import ru.yandex.practicum.grpc.telemetry.event.SensorEventProto;
import ru.yandex.practicum.grpc.telemetry.event.TemperatureSensorProto;
import ru.yandex.practicum.kafka.telemetry.event.SensorEventAvro;
import ru.yandex.practicum.kafka.telemetry.event.TemperatureSensorAvro;

@Component
@RequiredArgsConstructor
public class TemperatureSensorEventHandler extends SensorEventHandler {
    private final EventsService eventsService;

    @Override
    public SensorEventProto.PayloadCase getMessageTypeRPC() {
        return SensorEventProto.PayloadCase.TEMPERATURE_SENSOR;
    }

    @Override
    public SensorEventType getMessageTypeHTTP() {
        return SensorEventType.TEMPERATURE_SENSOR_EVENT;
    }

    @Override
    public void handle(SensorEventProto sensorEvent) {
        SensorEventAvro message = SensorEventAvro.newBuilder()
                .setHubId(sensorEvent.getHubId())
                .setId(sensorEvent.getId())
                .setTimestamp(getInstant(sensorEvent.getTimestamp()))
                .setPayload(getTemperatureSensorAvro(sensorEvent.getTemperatureSensor()))
                .setType(ru.yandex.practicum.kafka.telemetry.event.SensorEventType.TEMPERATURE_SENSOR_EVENT)
                .build();
        eventsService.collectSensorEvent(message);
    }

    @Override
    public void handle(SensorEvent sensorEvent) {
        SensorEventAvro message = SensorEventAvro.newBuilder()
                .setHubId(sensorEvent.getHubId())
                .setId(sensorEvent.getId())
                .setTimestamp(sensorEvent.getTimestamp())
                .setPayload(getTemperatureSensorAvro((TemperatureSensorEvent) sensorEvent))
                .setType(ru.yandex.practicum.kafka.telemetry.event.SensorEventType.TEMPERATURE_SENSOR_EVENT)
                .build();
        eventsService.collectSensorEvent(message);
    }

    private TemperatureSensorAvro getTemperatureSensorAvro(TemperatureSensorProto temperatureSensorProto) {
        return TemperatureSensorAvro.newBuilder()
                .setTemperatureF(temperatureSensorProto.getTemperatureF())
                .setTemperatureC(temperatureSensorProto.getTemperatureC())
                .build();
    }

    private TemperatureSensorAvro getTemperatureSensorAvro(TemperatureSensorEvent temperatureSensorEvent) {
        return TemperatureSensorAvro.newBuilder()
                .setTemperatureF(temperatureSensorEvent.getTemperatureF())
                .setTemperatureC(temperatureSensorEvent.getTemperatureC())
                .build();
    }
}
