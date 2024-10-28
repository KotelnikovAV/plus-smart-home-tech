package ru.practicum.events.hendlers.sensor;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
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
    public SensorEventProto.PayloadCase getMessageType() {
        return SensorEventProto.PayloadCase.TEMPERATURE_SENSOR;
    }

    @Override
    public void handle(SensorEventProto sensorEvent) {
        SensorEventAvro message = SensorEventAvro.newBuilder()
                .setHubId(sensorEvent.getHubId())
                .setId(sensorEvent.getId())
                .setTimestamp(getInstant(sensorEvent.getTimestamp()))
                .setPayload(getTemperatureSensorAvro(sensorEvent.getTemperatureSensor()))
                .build();
        eventsService.handleSensorEvent(message);
    }

    private TemperatureSensorAvro getTemperatureSensorAvro(TemperatureSensorProto temperatureSensorProto) {
        return TemperatureSensorAvro.newBuilder()
                .setTemperatureF(temperatureSensorProto.getTemperatureF())
                .setTemperatureC(temperatureSensorProto.getTemperatureC())
                .build();
    }
}
