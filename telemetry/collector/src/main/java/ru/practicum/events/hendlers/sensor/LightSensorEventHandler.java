package ru.practicum.events.hendlers.sensor;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import ru.practicum.events.service.EventsService;
import ru.yandex.practicum.grpc.telemetry.event.LightSensorProto;
import ru.yandex.practicum.grpc.telemetry.event.SensorEventProto;
import ru.yandex.practicum.kafka.telemetry.event.LightSensorAvro;
import ru.yandex.practicum.kafka.telemetry.event.SensorEventAvro;

@Component
@RequiredArgsConstructor
public class LightSensorEventHandler extends SensorEventHandler {
    private final EventsService eventsService;

    @Override
    public SensorEventProto.PayloadCase getMessageType() {
        return SensorEventProto.PayloadCase.LIGHT_SENSOR;
    }

    @Override
    public void handle(SensorEventProto sensorEvent) {
        SensorEventAvro message = SensorEventAvro.newBuilder()
                .setHubId(sensorEvent.getHubId())
                .setId(sensorEvent.getId())
                .setTimestamp(getInstant(sensorEvent.getTimestamp()))
                .setPayload(getLightSensorAvro(sensorEvent.getLightSensor()))
                .build();
        eventsService.handleSensorEvent(message);
    }

    private LightSensorAvro getLightSensorAvro(LightSensorProto lightSensorProto) {
        return LightSensorAvro.newBuilder()
                .setLuminosity(lightSensorProto.getLuminosity())
                .setLinkQuality(lightSensorProto.getLinkQuality())
                .build();
    }
}
