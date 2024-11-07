package ru.practicum.hub.handlers;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import ru.practicum.sensor.model.Sensor;
import ru.practicum.sensor.repository.SensorRepository;
import ru.yandex.practicum.kafka.telemetry.event.DeviceAddedEventAvro;
import ru.yandex.practicum.kafka.telemetry.event.DeviceTypeAvro;
import ru.yandex.practicum.kafka.telemetry.event.HubEventAvro;
import ru.yandex.practicum.kafka.telemetry.event.HubsEventTypeAvro;

@Component
@RequiredArgsConstructor
@Slf4j
public class DeviceAddedEventHandler implements HubEventHandler {
    private final SensorRepository sensorRepository;

    @Override
    public HubsEventTypeAvro getMessageType() {
        return HubsEventTypeAvro.DEVICE_ADDED_EVENT;
    }

    @Override
    public void handle(HubEventAvro hubEvent) {
        log.info("Received device added event: {}", hubEvent);
        DeviceAddedEventAvro deviceAddedEventAvro = (DeviceAddedEventAvro) hubEvent.getPayload();
        Sensor sensor = new Sensor();

        sensor.setId(deviceAddedEventAvro.getId());
        sensor.setType(DeviceTypeAvro.valueOf(deviceAddedEventAvro.getType().toString()));
        sensor.setHubId(hubEvent.getHubId());

        sensorRepository.save(sensor);
        log.info("Successfully added sensor: {}", sensor);
    }
}
