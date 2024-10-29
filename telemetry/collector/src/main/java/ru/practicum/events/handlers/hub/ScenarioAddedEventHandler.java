package ru.practicum.events.handlers.hub;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import ru.practicum.events.model.hub.DeviceAction;
import ru.practicum.events.model.hub.HubEvent;
import ru.practicum.events.model.hub.ScenarioAddedEvent;
import ru.practicum.events.model.hub.ScenarioCondition;
import ru.practicum.events.model.hub.enums.HubsEventType;
import ru.practicum.events.service.EventsService;
import ru.yandex.practicum.grpc.telemetry.event.DeviceActionProto;
import ru.yandex.practicum.grpc.telemetry.event.HubEventProto;
import ru.yandex.practicum.grpc.telemetry.event.ScenarioAddedEventProto;
import ru.yandex.practicum.grpc.telemetry.event.ScenarioConditionProto;
import ru.yandex.practicum.kafka.telemetry.event.*;

import java.util.ArrayList;
import java.util.List;

@Component
@RequiredArgsConstructor
public class ScenarioAddedEventHandler extends HubEventHandler {
    private final EventsService eventsService;

    @Override
    public HubEventProto.PayloadCase getMessageTypeRPC() {
        return HubEventProto.PayloadCase.SCENARIO_ADDED;
    }

    @Override
    public HubsEventType getMessageTypeHTTP() {
        return HubsEventType.SCENARIO_ADDED_EVENT;
    }

    @Override
    public void handle(HubEventProto hubEvent) {
        HubEventAvro message = HubEventAvro.newBuilder()
                .setHubId(hubEvent.getHubId())
                .setTimestamp(getInstant(hubEvent.getTimestamp()))
                .setPayload(getScenarioAddedEvent(hubEvent.getScenarioAdded()))
                .build();
        eventsService.collectHubEvent(message);
    }

    @Override
    public void handle(HubEvent hubEvent) {
        HubEventAvro message = HubEventAvro.newBuilder()
                .setHubId(hubEvent.getHubId())
                .setTimestamp(hubEvent.getTimestamp())
                .setPayload(getScenarioAddedEvent((ScenarioAddedEvent) hubEvent))
                .build();
        eventsService.collectHubEvent(message);
    }

    private ScenarioAddedEventAvro getScenarioAddedEvent(ScenarioAddedEventProto scenarioAddedEventProto) {
        return ScenarioAddedEventAvro.newBuilder()
                .setName(scenarioAddedEventProto.getName())
                .setActions(getDeviceActionsAvroRPC(scenarioAddedEventProto.getActionList()))
                .setConditions(getScenarioConditionsAvroRPC(scenarioAddedEventProto.getConditionList()))
                .build();
    }

    private ScenarioAddedEventAvro getScenarioAddedEvent(ScenarioAddedEvent scenarioAddedEvent) {
        return ScenarioAddedEventAvro.newBuilder()
                .setName(scenarioAddedEvent.getName())
                .setActions(getDeviceActionsAvroHTTP(scenarioAddedEvent.getActions()))
                .setConditions(getScenarioConditionsAvroHTTP(scenarioAddedEvent.getConditions()))
                .build();
    }

    private List<ScenarioConditionAvro> getScenarioConditionsAvroRPC(List<ScenarioConditionProto> scenarioConditionsProto) {
        List<ScenarioConditionAvro> scenarioConditionsAvro = new ArrayList<>();
        for (ScenarioConditionProto scenarioConditionProto : scenarioConditionsProto) {
            ScenarioConditionAvro scenarioConditionAvro = ScenarioConditionAvro.newBuilder()
                    .setOperation(ConditionOperationAvro.valueOf(scenarioConditionProto.getOperation().toString()))
                    .setType(ConditionTypeAvro.valueOf(scenarioConditionProto.getType().toString()))
                    .setSensorId(scenarioConditionProto.getSensorId())
                    .setValue(scenarioConditionProto.getBoolValue())
                    .build();
            scenarioConditionsAvro.add(scenarioConditionAvro);
        }
        return scenarioConditionsAvro;
    }

    private List<ScenarioConditionAvro> getScenarioConditionsAvroHTTP(List<ScenarioCondition> scenarioConditions) {
        List<ScenarioConditionAvro> scenarioConditionsAvro = new ArrayList<>();
        for (ScenarioCondition scenarioCondition : scenarioConditions) {
            ScenarioConditionAvro scenarioConditionAvro = ScenarioConditionAvro.newBuilder()
                    .setOperation(ConditionOperationAvro.valueOf(scenarioCondition.getOperation().toString()))
                    .setType(ConditionTypeAvro.valueOf(scenarioCondition.getType().toString()))
                    .setSensorId(scenarioCondition.getSensorId())
                    .setValue(scenarioCondition.getValue())
                    .build();
            scenarioConditionsAvro.add(scenarioConditionAvro);
        }
        return scenarioConditionsAvro;
    }

    private List<DeviceActionAvro> getDeviceActionsAvroRPC(List<DeviceActionProto> deviceActionsProto) {
        List<DeviceActionAvro> deviceActionsAvro = new ArrayList<>();
        for (DeviceActionProto deviceActionProto : deviceActionsProto) {
            DeviceActionAvro deviceActionAvro = DeviceActionAvro.newBuilder()
                    .setType(ActionTypeAvro.valueOf(deviceActionProto.getType().toString()))
                    .setValue(deviceActionProto.getValue())
                    .setSensorId(deviceActionProto.getSensorId())
                    .build();
            deviceActionsAvro.add(deviceActionAvro);
        }
        return deviceActionsAvro;
    }

    private List<DeviceActionAvro> getDeviceActionsAvroHTTP(List<DeviceAction> deviceActions) {
        List<DeviceActionAvro> deviceActionsAvro = new ArrayList<>();
        for (DeviceAction deviceAction : deviceActions) {
            DeviceActionAvro deviceActionAvro = DeviceActionAvro.newBuilder()
                    .setType(ActionTypeAvro.valueOf(deviceAction.getType().toString()))
                    .setValue(deviceAction.getValue())
                    .setSensorId(deviceAction.getSensorId())
                    .build();
            deviceActionsAvro.add(deviceActionAvro);
        }
        return deviceActionsAvro;
    }
}
