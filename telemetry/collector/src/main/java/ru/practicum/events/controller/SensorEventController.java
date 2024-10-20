package ru.practicum.events.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.practicum.events.model.sensor.SensorEvent;
import ru.practicum.events.service.EventsService;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("events/sensors")
public class SensorEventController {
    private final EventsService eventsService;

    @PostMapping
    public void collectSensorEvent(@Valid @RequestBody SensorEvent sensorEvent) {
        log.info("Received sensor event: {}", sensorEvent);
        eventsService.collectSensorEvent(sensorEvent);
    }
}
