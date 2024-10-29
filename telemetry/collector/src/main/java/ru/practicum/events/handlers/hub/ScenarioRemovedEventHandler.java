package ru.practicum.events.handlers.hub;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import ru.practicum.events.model.hub.HubEvent;
import ru.practicum.events.model.hub.ScenarioRemovedEvent;
import ru.practicum.events.model.hub.enums.HubsEventType;
import ru.practicum.events.service.EventsService;
import ru.yandex.practicum.grpc.telemetry.event.HubEventProto;
import ru.yandex.practicum.grpc.telemetry.event.ScenarioRemovedEventProto;
import ru.yandex.practicum.kafka.telemetry.event.HubEventAvro;
import ru.yandex.practicum.kafka.telemetry.event.ScenarioRemovedEventAvro;

@Component
@RequiredArgsConstructor
public class ScenarioRemovedEventHandler extends HubEventHandler {
    private final EventsService eventsService;

    @Override
    public HubEventProto.PayloadCase getMessageTypeRPC() {
        return HubEventProto.PayloadCase.SCENARIO_REMOVED;
    }

    @Override
    public HubsEventType getMessageTypeHTTP() {
        return HubsEventType.SCENARIO_REMOVED_EVENT;
    }

    @Override
    public void handle(HubEventProto hubEvent) {
        HubEventAvro message = HubEventAvro.newBuilder()
                .setHubId(hubEvent.getHubId())
                .setTimestamp(getInstant(hubEvent.getTimestamp()))
                .setPayload(getScenarioRemovedEvent(hubEvent.getScenarioRemoved()))
                .build();
        eventsService.collectHubEvent(message);
    }

    @Override
    public void handle(HubEvent hubEvent) {
        HubEventAvro message = HubEventAvro.newBuilder()
                .setHubId(hubEvent.getHubId())
                .setTimestamp(hubEvent.getTimestamp())
                .setPayload(getScenarioRemovedEvent((ScenarioRemovedEvent) hubEvent))
                .build();
        eventsService.collectHubEvent(message);
    }

    private ScenarioRemovedEventAvro getScenarioRemovedEvent(ScenarioRemovedEventProto scenarioRemovedEventProto) {
        return ScenarioRemovedEventAvro.newBuilder()
                .setName(scenarioRemovedEventProto.getName())
                .build();
    }

    private ScenarioRemovedEventAvro getScenarioRemovedEvent(ScenarioRemovedEvent scenarioRemovedEvent) {
        return ScenarioRemovedEventAvro.newBuilder()
                .setName(scenarioRemovedEvent.getName())
                .build();
    }
}