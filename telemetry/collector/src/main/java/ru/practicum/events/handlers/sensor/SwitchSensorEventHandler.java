package ru.practicum.events.handlers.sensor;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import ru.practicum.events.model.sensor.SensorEvent;
import ru.practicum.events.model.sensor.SwitchSensorEvent;
import ru.practicum.events.model.sensor.enums.SensorEventType;
import ru.practicum.events.producer.EventsProducer;
import ru.yandex.practicum.grpc.telemetry.event.SensorEventProto;
import ru.yandex.practicum.grpc.telemetry.event.SwitchSensorProto;
import ru.yandex.practicum.kafka.telemetry.event.SensorEventAvro;
import ru.yandex.practicum.kafka.telemetry.event.SensorEventTypeAvro;
import ru.yandex.practicum.kafka.telemetry.event.SwitchSensorAvro;

@Component
@RequiredArgsConstructor
public class SwitchSensorEventHandler extends SensorEventHandler {
    private final EventsProducer eventsProducer;

    @Override
    public SensorEventProto.PayloadCase getMessageTypeRPC() {
        return SensorEventProto.PayloadCase.SWITCH_SENSOR;
    }

    @Override
    public SensorEventType getMessageTypeHTTP() {
        return SensorEventType.SWITCH_SENSOR_EVENT;
    }

    @Override
    public void handle(SensorEventProto sensorEvent) {
        SensorEventAvro message = SensorEventAvro.newBuilder()
                .setHubId(sensorEvent.getHubId())
                .setId(sensorEvent.getId())
                .setTimestamp(getInstant(sensorEvent.getTimestamp()))
                .setPayload(getSwitchSensorAvro(sensorEvent.getSwitchSensor()))
                .setType(SensorEventTypeAvro.SWITCH_SENSOR_EVENT)
                .build();
        eventsProducer.collectSensorEvent(message);
    }

    @Override
    public void handle(SensorEvent sensorEvent) {
        SensorEventAvro message = SensorEventAvro.newBuilder()
                .setHubId(sensorEvent.getHubId())
                .setId(sensorEvent.getId())
                .setTimestamp(sensorEvent.getTimestamp())
                .setPayload(getSwitchSensorAvro((SwitchSensorEvent) sensorEvent))
                .setType(SensorEventTypeAvro.SWITCH_SENSOR_EVENT)
                .build();
        eventsProducer.collectSensorEvent(message);
    }

    private SwitchSensorAvro getSwitchSensorAvro(SwitchSensorProto switchSensorProto) {
        return SwitchSensorAvro.newBuilder()
                .setState(switchSensorProto.getState())
                .build();
    }

    private SwitchSensorAvro getSwitchSensorAvro(SwitchSensorEvent switchSensorEvent) {
        return SwitchSensorAvro.newBuilder()
                .setState(switchSensorEvent.getState())
                .build();
    }
}
