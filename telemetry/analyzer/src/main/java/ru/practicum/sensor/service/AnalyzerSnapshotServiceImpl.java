package ru.practicum.sensor.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.hub.model.ScenarioCondition;
import ru.practicum.hub.repository.ScenarioActionRepository;
import ru.practicum.hub.repository.ScenarioConditionalRepository;
import ru.yandex.practicum.kafka.telemetry.event.SensorsSnapshotAvro;

import java.util.List;
import java.util.Set;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class AnalyzerSnapshotServiceImpl implements AnalyzerSnapshotService {
    private final ScenarioActionRepository scenarioActionRepository;
    private final ScenarioConditionalRepository scenarioConditionalRepository;

    @Override
    public void handleSnapshot(SensorsSnapshotAvro snapshot) {
        Set<String> idSensors = snapshot.getSensorsState().keySet();
        List<ScenarioCondition> scenarioCondition = scenarioConditionalRepository.findAll();
        scenarioCondition = scenarioCondition.stream()
                .filter(condition -> idSensors.contains(condition.getId().getSensor().getId()))
                .toList();
    }
}
