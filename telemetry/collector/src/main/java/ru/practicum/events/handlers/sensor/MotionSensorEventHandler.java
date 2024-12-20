package ru.practicum.events.handlers.sensor;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import ru.practicum.events.model.sensor.MotionSensorEvent;
import ru.practicum.events.model.sensor.SensorEvent;
import ru.practicum.events.model.sensor.enums.SensorEventType;
import ru.practicum.events.producer.EventsProducer;
import ru.yandex.practicum.grpc.telemetry.event.MotionSensorProto;
import ru.yandex.practicum.grpc.telemetry.event.SensorEventProto;
import ru.yandex.practicum.kafka.telemetry.event.MotionSensorAvro;
import ru.yandex.practicum.kafka.telemetry.event.SensorEventAvro;
import ru.yandex.practicum.kafka.telemetry.event.SensorEventTypeAvro;

@Component
@RequiredArgsConstructor
public class MotionSensorEventHandler extends SensorEventHandler {
    private final EventsProducer eventsProducer;

    @Override
    public SensorEventProto.PayloadCase getMessageTypeRPC() {
        return SensorEventProto.PayloadCase.MOTION_SENSOR;
    }

    @Override
    public SensorEventType getMessageTypeHTTP() {
        return SensorEventType.MOTION_SENSOR_EVENT;
    }

    @Override
    public void handle(SensorEventProto sensorEvent) {
        SensorEventAvro message = SensorEventAvro.newBuilder()
                .setHubId(sensorEvent.getHubId())
                .setId(sensorEvent.getId())
                .setTimestamp(getInstant(sensorEvent.getTimestamp()))
                .setPayload(getMotionSensorAvro(sensorEvent.getMotionSensor()))
                .setType(SensorEventTypeAvro.MOTION_SENSOR_EVENT)
                .build();
        eventsProducer.collectSensorEvent(message);
    }

    @Override
    public void handle(SensorEvent sensorEvent) {
        SensorEventAvro message = SensorEventAvro.newBuilder()
                .setHubId(sensorEvent.getHubId())
                .setId(sensorEvent.getId())
                .setTimestamp(sensorEvent.getTimestamp())
                .setPayload(getMotionSensorAvro((MotionSensorEvent) sensorEvent))
                .setType(SensorEventTypeAvro.MOTION_SENSOR_EVENT)
                .build();
        eventsProducer.collectSensorEvent(message);
    }

    private MotionSensorAvro getMotionSensorAvro(MotionSensorProto motionSensorProto) {
        return MotionSensorAvro.newBuilder()
                .setVoltage(motionSensorProto.getVoltage())
                .setMotion(motionSensorProto.getMotion())
                .setLinkQuality(motionSensorProto.getLinkQuality())
                .build();
    }

    private MotionSensorAvro getMotionSensorAvro(MotionSensorEvent motionSensorEvent) {
        return MotionSensorAvro.newBuilder()
                .setVoltage(motionSensorEvent.getVoltage())
                .setMotion(motionSensorEvent.getMotion())
                .setLinkQuality(motionSensorEvent.getLinkQuality())
                .build();
    }
}
