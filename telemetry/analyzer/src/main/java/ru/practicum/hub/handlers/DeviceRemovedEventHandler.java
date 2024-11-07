package ru.practicum.hub.handlers;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import ru.practicum.sensor.repository.SensorRepository;
import ru.yandex.practicum.kafka.telemetry.event.DeviceRemovedEventAvro;
import ru.yandex.practicum.kafka.telemetry.event.HubEventAvro;
import ru.yandex.practicum.kafka.telemetry.event.HubsEventTypeAvro;

@Component
@RequiredArgsConstructor
@Slf4j
public class DeviceRemovedEventHandler implements HubEventHandler {
    private final SensorRepository sensorRepository;

    @Override
    public HubsEventTypeAvro getMessageType() {
        return HubsEventTypeAvro.DEVICE_REMOVED_EVENT;
    }

    @Override
    public void handle(HubEventAvro hubEvent) {
        log.info("Received device removed event: {}", hubEvent);
        DeviceRemovedEventAvro deviceRemovedEventAvro = (DeviceRemovedEventAvro) hubEvent.getPayload();
        sensorRepository.deleteById(deviceRemovedEventAvro.getId());
        log.info("Device removed: {}", deviceRemovedEventAvro);
    }
}
