package ru.practicum.hub.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.practicum.hub.model.Scenario;

public interface ScenarioRepository extends JpaRepository<Scenario, Long> {
    void deleteByName(String scenarioName);
}
