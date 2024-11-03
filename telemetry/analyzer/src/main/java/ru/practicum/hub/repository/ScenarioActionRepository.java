package ru.practicum.hub.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.practicum.hub.model.ScenarioAction;
import ru.practicum.hub.model.ScenarioActionId;

public interface ScenarioActionRepository extends JpaRepository<ScenarioAction, ScenarioActionId> {
}
