package ru.practicum.events.mapper;

import lombok.experimental.UtilityClass;
import org.apache.avro.specific.SpecificRecordBase;
import ru.practicum.events.model.hub.*;
import ru.yandex.practicum.kafka.telemetry.event.*;

import java.util.ArrayList;
import java.util.List;

@UtilityClass
public class HubEventMapper {
    public SpecificRecordBase getHubEventAvro(HubEvent hubEvent) {
        switch (hubEvent.getType()) {
            case DEVICE_ADDED_EVENT -> {
                return DeviceAddedEventAvro.newBuilder()
                        .setId(((DeviceAddedEvent) hubEvent).getId())
                        .setType(getDeviceTypeAvro(hubEvent))
                        .build();
            }
            case DEVICE_REMOVED_EVENT -> {
                return DeviceRemovedEventAvro.newBuilder()
                        .setId(((DeviceRemovedEvent) hubEvent).getId())
                        .build();
            }
            case SCENARIO_ADDED_EVENT -> {
                return ScenarioAddedEventAvro.newBuilder()
                        .setConditions(getScenarioConditionsAvro(((ScenarioAddedEvent) hubEvent).getConditions()))
                        .setActions(getDeviceActionsAvro(((ScenarioAddedEvent) hubEvent).getActions()))
                        .setName(((ScenarioAddedEvent) hubEvent).getName())
                        .build();
            }
            case SCENARIO_REMOVED_EVENT -> {
                return ScenarioRemovedEventAvro.newBuilder()
                        .setName(((ScenarioAddedEvent) hubEvent).getName())
                        .build();
            }
        }
        return null;
    }

    public DeviceTypeAvro getDeviceTypeAvro(HubEvent hubEvent) {
        DeviceAddedEvent deviceAddedEvent = (DeviceAddedEvent) hubEvent;
        switch (deviceAddedEvent.getDeviceType()) {
            case LIGHT_SENSOR -> {
                return DeviceTypeAvro.LIGHT_SENSOR;
            }
            case MOTION_SENSOR -> {
                return DeviceTypeAvro.MOTION_SENSOR;
            }
            case SWITCH_SENSOR -> {
                return DeviceTypeAvro.SWITCH_SENSOR;
            }
            case CLIMATE_SENSOR -> {
                return DeviceTypeAvro.CLIMATE_SENSOR;
            }
            case TEMPERATURE_SENSOR -> {
                return DeviceTypeAvro.TEMPERATURE_SENSOR;
            }
        }
        return null;
    }

    public List<ScenarioConditionAvro> getScenarioConditionsAvro(List<ScenarioCondition> conditions) {
        List<ScenarioConditionAvro> scenarioConditionsAvro = new ArrayList<>();
        for (ScenarioCondition scenarioCondition : conditions) {
            ScenarioConditionAvro scenarioConditionAvro = ScenarioConditionAvro.newBuilder()
                    .setType(getConditionTypeAvro(scenarioCondition))
                    .setOperation(getConditionOperationAvro(scenarioCondition))
                    .build();
            scenarioConditionsAvro.add(scenarioConditionAvro);
        }
        return scenarioConditionsAvro;
    }

    public ConditionTypeAvro getConditionTypeAvro(ScenarioCondition scenarioCondition) {
        switch (scenarioCondition.getType()) {
            case MOTION -> {
                return ConditionTypeAvro.MOTION;
            }
            case SWITCH -> {
                return ConditionTypeAvro.SWITCH;
            }
            case CO2LEVEL -> {
                return ConditionTypeAvro.CO2LEVEL;
            }
            case HUMIDITY -> {
                return ConditionTypeAvro.HUMIDITY;
            }
            case LUMINOSITY -> {
                return ConditionTypeAvro.LUMINOSITY;
            }
            case TEMPERATURE -> {
                return ConditionTypeAvro.TEMPERATURE;
            }
        }
        return null;
    }

    public ConditionOperationAvro getConditionOperationAvro(ScenarioCondition scenarioCondition) {
        switch (scenarioCondition.getOperation()) {
            case EQUALS -> {
                return ConditionOperationAvro.EQUALS;
            }
            case GREATER_THAN -> {
                return ConditionOperationAvro.GREATER_THAN;
            }
            case LOWER_THAN -> {
                return ConditionOperationAvro.LOWER_THAN;
            }
        }
        return null;
    }

    public List<DeviceActionAvro> getDeviceActionsAvro(List<DeviceAction> deviceActions) {
        List<DeviceActionAvro> deviceActionsAvro = new ArrayList<>();
        for (DeviceAction deviceAction : deviceActions) {
            DeviceActionAvro deviceActionAvro = DeviceActionAvro.newBuilder()
                    .setSensorId(deviceAction.getSensorId())
                    .setValue(deviceAction.getValue())
                    .setType(getActionTypeAvro(deviceAction))
                    .build();
            deviceActionsAvro.add(deviceActionAvro);
        }
        return deviceActionsAvro;
    }

    public ActionTypeAvro getActionTypeAvro(DeviceAction deviceAction) {
        switch (deviceAction.getType()) {
            case INVERSE -> {
                return ActionTypeAvro.INVERSE;
            }
            case ACTIVATE -> {
                return ActionTypeAvro.ACTIVATE;
            }
            case SET_VALUE -> {
                return ActionTypeAvro.SET_VALUE;
            }
            case DEACTIVATE -> {
                return ActionTypeAvro.DEACTIVATE;
            }
        }
        return null;
    }
}
