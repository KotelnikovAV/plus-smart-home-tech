package ru.practicum.hub.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.practicum.hub.model.ScenarioActionId;
import ru.practicum.hub.model.ScenarioCondition;

public interface ScenarioConditionalRepository extends JpaRepository<ScenarioCondition, ScenarioActionId> {
//    List<ScenarioCondition> findBySensorIdIn(Set<String> sensorId);
}
