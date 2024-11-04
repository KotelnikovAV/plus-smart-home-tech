package ru.practicum.hub.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import ru.practicum.hub.model.ScenarioAction;
import ru.practicum.hub.model.ScenarioActionId;

import java.util.List;

public interface ScenarioActionRepository extends JpaRepository<ScenarioAction, ScenarioActionId> {

    @Query("select sa " +
            "from ScenarioAction as sa " +
            "join sa.id.action as a " +
            "join sa.id.sensor as s " +
            "where sa.id.scenario.id = :scenario_id ")
    List<ScenarioAction> findByScenarioId(@Param("scenario_id") Long scenarioId);
}
