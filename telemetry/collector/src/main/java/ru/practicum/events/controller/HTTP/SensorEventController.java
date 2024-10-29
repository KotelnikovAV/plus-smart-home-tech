package ru.practicum.events.controller.HTTP;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.practicum.configuration.ConfigurationHandlers;
import ru.practicum.events.model.sensor.SensorEvent;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("events/sensors")
public class SensorEventController {
    private final ConfigurationHandlers configurationHandlers;

    @PostMapping
    public void collectSensorEvent(@Valid @RequestBody SensorEvent sensorEvent) {
        log.info("Received sensor event: {}", sensorEvent);
        if (configurationHandlers.getSensorEventHandlersHTTP().containsKey(sensorEvent.getType())) {
            configurationHandlers.getSensorEventHandlersHTTP().get(sensorEvent.getType()).handle(sensorEvent);
        } else {
            throw new IllegalArgumentException("There is no handler for the event " + sensorEvent.getType());
        }
    }
}