package ru.practicum.configuration;

import lombok.Getter;
import lombok.Setter;
import org.springframework.stereotype.Component;
import ru.practicum.sensor.handlers.SnapshotHandler;
import ru.yandex.practicum.kafka.telemetry.event.HubsEventTypeAvro;
import ru.yandex.practicum.kafka.telemetry.event.SensorEventTypeAvro;

import java.util.Map;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;

@Getter
@Setter
@Component
public class ConfigurationHandlers {
    private final Map<HubsEventTypeAvro, ru.practicum.hub.handlers.HubEventHandler> hubEventHandlers;
    private final Map<SensorEventTypeAvro, SnapshotHandler> snapshotHandlers;

    public ConfigurationHandlers(Set<ru.practicum.hub.handlers.HubEventHandler> hubEventHandlers,
                                 Set<SnapshotHandler> snapshotHandlers) {
        this.hubEventHandlers = hubEventHandlers.stream()
                .collect(Collectors.toMap(
                        ru.practicum.hub.handlers.HubEventHandler::getMessageType,
                        Function.identity()
                ));
        this.snapshotHandlers = snapshotHandlers.stream()
                .collect(Collectors.toMap(
                        SnapshotHandler::getMessageType,
                        Function.identity()
                ));
    }
}