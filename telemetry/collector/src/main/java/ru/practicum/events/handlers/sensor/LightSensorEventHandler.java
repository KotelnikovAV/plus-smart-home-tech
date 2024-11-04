package ru.practicum.events.handlers.sensor;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import ru.practicum.events.model.sensor.LightSensorEvent;
import ru.practicum.events.model.sensor.SensorEvent;
import ru.practicum.events.model.sensor.enums.SensorEventType;
import ru.practicum.events.service.EventsService;
import ru.yandex.practicum.grpc.telemetry.event.LightSensorProto;
import ru.yandex.practicum.grpc.telemetry.event.SensorEventProto;
import ru.yandex.practicum.kafka.telemetry.event.LightSensorAvro;
import ru.yandex.practicum.kafka.telemetry.event.SensorEventAvro;
import ru.yandex.practicum.kafka.telemetry.event.SensorEventTypeAvro;

@Component
@RequiredArgsConstructor
public class LightSensorEventHandler extends SensorEventHandler {
    private final EventsService eventsService;

    @Override
    public SensorEventProto.PayloadCase getMessageTypeRPC() {
        return SensorEventProto.PayloadCase.LIGHT_SENSOR;
    }

    @Override
    public SensorEventType getMessageTypeHTTP() {
        return SensorEventType.LIGHT_SENSOR_EVENT;
    }

    @Override
    public void handle(SensorEventProto sensorEvent) {
        SensorEventAvro message = SensorEventAvro.newBuilder()
                .setHubId(sensorEvent.getHubId())
                .setId(sensorEvent.getId())
                .setTimestamp(getInstant(sensorEvent.getTimestamp()))
                .setPayload(getLightSensorAvro(sensorEvent.getLightSensor()))
                .setType(SensorEventTypeAvro.LIGHT_SENSOR_EVENT)
                .build();
        eventsService.collectSensorEvent(message);
    }

    @Override
    public void handle(SensorEvent sensorEvent) {
        SensorEventAvro message = SensorEventAvro.newBuilder()
                .setHubId(sensorEvent.getHubId())
                .setId(sensorEvent.getId())
                .setTimestamp(sensorEvent.getTimestamp())
                .setPayload(getLightSensorAvro((LightSensorEvent) sensorEvent))
                .setType(SensorEventTypeAvro.LIGHT_SENSOR_EVENT)
                .build();
        eventsService.collectSensorEvent(message);
    }

    private LightSensorAvro getLightSensorAvro(LightSensorProto lightSensorProto) {
        return LightSensorAvro.newBuilder()
                .setLuminosity(lightSensorProto.getLuminosity())
                .setLinkQuality(lightSensorProto.getLinkQuality())
                .build();
    }

    private LightSensorAvro getLightSensorAvro(LightSensorEvent lightSensorEvent) {
        return LightSensorAvro.newBuilder()
                .setLuminosity(lightSensorEvent.getLuminosity())
                .setLinkQuality(lightSensorEvent.getLinkQuality())
                .build();
    }
}
