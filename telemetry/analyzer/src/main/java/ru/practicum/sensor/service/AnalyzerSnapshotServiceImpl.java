package ru.practicum.sensor.service;

import com.google.protobuf.Timestamp;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.client.HubRouterClient;
import ru.practicum.configuration.ConfigurationHandlers;
import ru.practicum.hub.model.Condition;
import ru.practicum.hub.model.Scenario;
import ru.practicum.hub.model.ScenarioAction;
import ru.practicum.hub.model.ScenarioActionId;
import ru.practicum.hub.repository.ScenarioActionRepository;
import ru.practicum.hub.repository.ScenarioConditionalRepository;
import ru.yandex.practicum.grpc.telemetry.event.ActionTypeProto;
import ru.yandex.practicum.grpc.telemetry.event.DeviceActionProto;
import ru.yandex.practicum.grpc.telemetry.event.DeviceActionRequest;
import ru.yandex.practicum.kafka.telemetry.event.SensorStateAvro;
import ru.yandex.practicum.kafka.telemetry.event.SensorsSnapshotAvro;

import java.time.Instant;
import java.util.List;
import java.util.Map;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class AnalyzerSnapshotServiceImpl implements AnalyzerSnapshotService {
    private final ScenarioConditionalRepository conditionalRepository;
    private final ScenarioActionRepository actionRepository;
    private final ConfigurationHandlers configurationHandlers;
    private final HubRouterClient hubRouterClient;

    @Override
    public void handleSnapshot(SensorsSnapshotAvro snapshot) {
        log.info("Handling snapshot {}", snapshot);

        Map<String, SensorStateAvro> sensors = snapshot.getSensorsState();
        List<Scenario> scenarios = conditionalRepository.findScenarios();

        for (Scenario scenario : scenarios) {
            boolean checkConditions = true;
            List<Condition> conditions = conditionalRepository.findConditions(scenario.getId());
            for (Condition condition : conditions) {
                SensorStateAvro sensorStateAvro = sensors.get(condition.getSensor().getId());
                checkConditions = checkConditions && configurationHandlers.getSnapshotHandlers()
                        .get(sensorStateAvro.getType())
                        .handle(condition, sensorStateAvro);
            }
            if (checkConditions) {
                sendAction(scenario);
            }
        }
    }

    private void sendAction(Scenario scenario) {
        log.info("Sending actions for scenario {}", scenario);
        List<ScenarioActionId> scenarioActions = actionRepository.findByScenarioId(scenario.getId()).stream()
                .map(ScenarioAction::getId)
                .toList();

        for (ScenarioActionId action : scenarioActions) {
            Timestamp timestamp = Timestamp.newBuilder()
                    .setNanos(Instant.now().getNano())
                    .setSeconds(Instant.now().getEpochSecond())
                    .build();
            DeviceActionProto deviceActionProto = DeviceActionProto.newBuilder()
                    .setType(ActionTypeProto.valueOf(action.getAction().getType().toString()))
                    .setSensorId(action.getSensor().getId())
                    .setValue(action.getAction().getAmount())
                    .build();

            DeviceActionRequest deviceActionRequest = DeviceActionRequest.newBuilder()
                    .setHubId(scenario.getHubId())
                    .setTimestamp(timestamp)
                    .setAction(deviceActionProto)
                    .setScenarioName(scenario.getName())
                    .build();

            hubRouterClient.handleDeviceAction(deviceActionRequest);

            log.info("The action has been sent {}", deviceActionRequest);
        }
    }
}
