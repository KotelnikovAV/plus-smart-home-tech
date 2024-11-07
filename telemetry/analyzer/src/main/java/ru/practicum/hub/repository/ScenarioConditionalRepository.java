package ru.practicum.hub.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import ru.practicum.hub.model.Condition;
import ru.practicum.hub.model.Scenario;
import ru.practicum.hub.model.ScenarioActionId;
import ru.practicum.hub.model.ScenarioCondition;

import java.util.List;

public interface ScenarioConditionalRepository extends JpaRepository<ScenarioCondition, ScenarioActionId> {

    @Query("select sc.id.scenario " +
            "from ScenarioCondition as sc " +
            "join sc.id.scenario as s ")
    List<Scenario> findScenarios();

    @Query("select sc.id.condition " +
            "from ScenarioCondition as sc " +
            "join sc.id.condition as s " +
            "where sc.id.scenario.id = :scenario_id ")
    List<Condition> findConditions(@Param("scenario_id") Long scenarioId);

}
