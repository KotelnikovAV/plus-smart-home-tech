package ru.practicum.hub.handlers;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import ru.practicum.exception.NotFoundException;
import ru.practicum.hub.mapper.ActionMapper;
import ru.practicum.hub.mapper.ConditionMapper;
import ru.practicum.hub.model.*;
import ru.practicum.hub.repository.*;
import ru.practicum.sensor.repository.SensorRepository;
import ru.yandex.practicum.kafka.telemetry.event.*;

import java.util.ArrayList;
import java.util.List;

@Component
@RequiredArgsConstructor
@Slf4j
public class ScenarioAddedEventHandler implements HubEventHandler {
    private final ActionRepository actionRepository;
    private final ConditionRepository conditionRepository;
    private final ScenarioConditionalRepository scenarioConditionalRepository;
    private final ScenarioRepository scenarioRepository;
    private final ScenarioActionRepository scenarioActionRepository;
    private final SensorRepository sensorRepository;
    private final ConditionMapper conditionMapper;
    private final ActionMapper actionMapper;

    @Override
    public HubsEventTypeAvro getMessageType() {
        return HubsEventTypeAvro.SCENARIO_ADDED_EVENT;
    }

    @Override
    public void handle(HubEventAvro hubEvent) {
        log.info("Received scenario added event: {}", hubEvent);

        Scenario scenario = saveScenario(hubEvent);
        ScenarioAddedEventAvro scenarioAddedEventAvro = (ScenarioAddedEventAvro) hubEvent.getPayload();
        List<ScenarioConditionAvro> scenarioConditionsAvro = scenarioAddedEventAvro.getConditions();
        List<DeviceActionAvro> deviceActionsAvro = scenarioAddedEventAvro.getActions();

        checkSensors(scenarioConditionsAvro);
        List<Condition> conditions = saveCondition(scenarioConditionsAvro);
        List<Action> actions = saveAction(deviceActionsAvro);
        saveScenarioConditions(scenario, conditions);
        saveScenarioActions(scenario, actions);
    }

    private void checkSensors(List<ScenarioConditionAvro> scenarioConditionsAvro) {
        boolean check = scenarioConditionsAvro.stream()
                .map(scenarioConditionAvro ->  sensorRepository.existsById(scenarioConditionAvro.getSensorId()))
                .reduce(Boolean::logicalAnd).orElse(false);

        if (!check) {
            throw new NotFoundException("Sensor not found");
        }
    }

    private List<Condition> saveCondition(List<ScenarioConditionAvro> scenarioConditionsAvro) {
        log.info("Saving conditions: {}", scenarioConditionsAvro);

        List<Condition> conditions = conditionMapper.conditionAvroListToConditionList(scenarioConditionsAvro);
        conditions = conditions.stream()
                .peek(condition -> condition.setSensor(sensorRepository.findById(condition.getSensor().getId()).get()))
                .toList();
        conditions = conditionRepository.saveAll(conditions);

        log.info("Saved conditions: {}", conditions);
        return conditions;
    }

    private Scenario saveScenario(HubEventAvro hubEvent) {
        log.info("Saving scenario: {}", hubEvent);

        Scenario scenario = new Scenario();
        scenario.setHubId(hubEvent.getHubId());
        scenario.setName(((ScenarioAddedEventAvro) hubEvent.getPayload()).getName());
        scenario = scenarioRepository.save(scenario);

        log.info("Saved scenario: {}", scenario);
        return scenario;
    }

    private List<Action> saveAction(List<DeviceActionAvro> deviceActionsAvro) {
        log.info("Saving actions: {}", deviceActionsAvro);

        List<Action> actions = actionMapper.actionAvroListToActionList(deviceActionsAvro);
        actions = actionRepository.saveAll(actions);

        log.info("Saved actions: {}", actions);
        return actions;
    }

    private void saveScenarioConditions(Scenario scenario, List<Condition> conditions) {
        log.info("Saving scenario conditions: {}", conditions);

        List<ScenarioConditionId> scenarioConditionsId = new ArrayList<>();

        conditions.forEach(condition ->  scenarioConditionsId.add(new ScenarioConditionId(scenario, condition)));
        List<ScenarioCondition> scenarioConditions = scenarioConditionsId.stream()
                .map(ScenarioCondition::new)
                .toList();

        scenarioConditionalRepository.saveAll(scenarioConditions);
        log.info("Saved scenario conditions: {}", scenarioConditions);
    }

    private void saveScenarioActions(Scenario scenario, List<Action> actions) {
        log.info("Saving scenario actions: {}", actions);

        List<ScenarioActionId> scenarioActionsId = new ArrayList<>();

        actions.forEach(action ->  scenarioActionsId
                .add(new ScenarioActionId(scenario,
                        sensorRepository.findById(action.getSensor().getId()).get(),
                        action)));

        List<ScenarioAction> scenarioActions = scenarioActionsId.stream()
                .map(ScenarioAction::new)
                .toList();

        scenarioActionRepository.saveAll(scenarioActions);
        log.info("Saved scenario actions: {}", scenarioActions);
    }
}
