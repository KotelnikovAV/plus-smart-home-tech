package ru.practicum.events.hendlers.sensor;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import ru.practicum.events.service.EventsService;
import ru.yandex.practicum.grpc.telemetry.event.MotionSensorProto;
import ru.yandex.practicum.grpc.telemetry.event.SensorEventProto;
import ru.yandex.practicum.kafka.telemetry.event.MotionSensorAvro;
import ru.yandex.practicum.kafka.telemetry.event.SensorEventAvro;

@Component
@RequiredArgsConstructor
public class MotionSensorEventHandler extends SensorEventHandler {
    private final EventsService eventsService;

    @Override
    public SensorEventProto.PayloadCase getMessageType() {
        return SensorEventProto.PayloadCase.MOTION_SENSOR;
    }

    @Override
    public void handle(SensorEventProto sensorEvent) {
        SensorEventAvro message = SensorEventAvro.newBuilder()
                .setHubId(sensorEvent.getHubId())
                .setId(sensorEvent.getId())
                .setTimestamp(getInstant(sensorEvent.getTimestamp()))
                .setPayload(getMotionSensorAvro(sensorEvent.getMotionSensor()))
                .build();
        eventsService.handleSensorEvent(message);
    }

    private MotionSensorAvro getMotionSensorAvro(MotionSensorProto motionSensorProto) {
        return MotionSensorAvro.newBuilder()
                .setVoltage(motionSensorProto.getVoltage())
                .setMotion(motionSensorProto.getMotion())
                .setLinkQuality(motionSensorProto.getLinkQuality())
                .build();
    }
}
