package ru.practicum.events.controller.http;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.practicum.events.mapper.SensorEventMapper;
import ru.practicum.events.model.sensor.SensorEvent;
import ru.practicum.events.service.EventsService;
import ru.yandex.practicum.kafka.telemetry.event.SensorEventAvro;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("events/sensors")
public class SensorEventController {
    private final EventsService eventsService;

    @PostMapping
    public void collectSensorEvent(@Valid @RequestBody SensorEvent sensorEvent) {
        log.info("Received sensor event: {}", sensorEvent);
        SensorEventAvro message = SensorEventAvro.newBuilder()
                .setHubId(sensorEvent.getHubId())
                .setId(sensorEvent.getId())
                .setTimestamp(sensorEvent.getTimestamp())
                .setPayload(SensorEventMapper.getSensorEventAvro(sensorEvent))
                .build();
        eventsService.handleSensorEvent(message);
    }
}
