package ru.practicum.configuration;

import lombok.Getter;
import lombok.Setter;
import org.springframework.stereotype.Component;
import ru.practicum.events.handlers.hub.HubEventHandler;
import ru.practicum.events.handlers.sensor.SensorEventHandler;
import ru.practicum.events.model.hub.enums.HubsEventType;
import ru.practicum.events.model.sensor.enums.SensorEventType;
import ru.yandex.practicum.grpc.telemetry.event.HubEventProto;
import ru.yandex.practicum.grpc.telemetry.event.SensorEventProto;

import java.util.Map;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;

@Getter
@Setter
@Component
public class ConfigurationHandlers {
    private final Map<SensorEventProto.PayloadCase, SensorEventHandler> sensorEventHandlersRPC;
    private final Map<HubEventProto.PayloadCase, HubEventHandler> hubEventHandlersRPC;
    private final Map<SensorEventType, SensorEventHandler> sensorEventHandlersHTTP;
    private final Map<HubsEventType, HubEventHandler> hubEventHandlersHTTP;

    public ConfigurationHandlers(Set<SensorEventHandler> sensorEventHandlers,
                               Set<HubEventHandler> hubEventHandlers) {
        this.sensorEventHandlersRPC = sensorEventHandlers.stream()
                .collect(Collectors.toMap(
                        SensorEventHandler::getMessageTypeRPC,
                        Function.identity()
                ));
        this.hubEventHandlersRPC = hubEventHandlers.stream()
                .collect(Collectors.toMap(
                        HubEventHandler::getMessageTypeRPC,
                        Function.identity()
                ));
        this.sensorEventHandlersHTTP = sensorEventHandlers.stream()
                .collect(Collectors.toMap(
                        SensorEventHandler::getMessageTypeHTTP,
                        Function.identity()
                ));
        this.hubEventHandlersHTTP = hubEventHandlers.stream()
                .collect(Collectors.toMap(
                        HubEventHandler::getMessageTypeHTTP,
                        Function.identity()
                ));
    }
}