package ru.practicum.events.service;

import ru.practicum.events.model.hub.HubEvent;
import ru.practicum.events.model.sensor.SensorEvent;

public interface EventsService {
    void collectSensorEvent(SensorEvent sensorEvent);

    void collectHubEvent(HubEvent hubEvent);
}
