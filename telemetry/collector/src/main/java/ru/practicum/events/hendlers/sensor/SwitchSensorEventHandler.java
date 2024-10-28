package ru.practicum.events.hendlers.sensor;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import ru.practicum.events.service.EventsService;
import ru.yandex.practicum.grpc.telemetry.event.SensorEventProto;
import ru.yandex.practicum.grpc.telemetry.event.SwitchSensorProto;
import ru.yandex.practicum.kafka.telemetry.event.SensorEventAvro;
import ru.yandex.practicum.kafka.telemetry.event.SwitchSensorAvro;

@Component
@RequiredArgsConstructor
public class SwitchSensorEventHandler extends SensorEventHandler {
    private final EventsService eventsService;

    @Override
    public SensorEventProto.PayloadCase getMessageType() {
        return SensorEventProto.PayloadCase.SWITCH_SENSOR;
    }

    @Override
    public void handle(SensorEventProto sensorEvent) {
        SensorEventAvro message = SensorEventAvro.newBuilder()
                .setHubId(sensorEvent.getHubId())
                .setId(sensorEvent.getId())
                .setTimestamp(getInstant(sensorEvent.getTimestamp()))
                .setPayload(getSwitchSensorAvro(sensorEvent.getSwitchSensor()))
                .build();
        eventsService.handleSensorEvent(message);
    }

    private SwitchSensorAvro getSwitchSensorAvro(SwitchSensorProto switchSensorProto) {
        return SwitchSensorAvro.newBuilder()
                .setState(switchSensorProto.getState())
                .build();
    }
}
