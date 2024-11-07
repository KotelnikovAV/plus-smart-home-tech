package ru.practicum.hub.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;
import ru.practicum.hub.model.Condition;
import ru.practicum.sensor.model.Sensor;
import ru.yandex.practicum.kafka.telemetry.event.ScenarioConditionAvro;

import java.util.List;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public abstract class ConditionMapper {

    public Condition conditionAvroToCondition(ScenarioConditionAvro scenarioConditionAvro) {
        Condition condition = new Condition();
        if (scenarioConditionAvro != null) {
            Sensor sensor = new Sensor();
            sensor.setId(scenarioConditionAvro.getSensorId());
            condition.setType(scenarioConditionAvro.getType());
            condition.setAmount(scenarioConditionAvro.getValue());
            condition.setSensor(sensor);
            return condition;
        }
        return null;
    }

    public abstract List<Condition> conditionAvroListToConditionList(List<ScenarioConditionAvro> scenarioConditionAvroList);
}
