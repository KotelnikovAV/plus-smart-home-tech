package ru.practicum.events.model.hub;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import ru.practicum.events.model.hub.enums.ConditionsType;
import ru.practicum.events.model.hub.enums.OperationType;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class ScenarioCondition {
    @NotBlank
    private String sensorId;
    @NotNull
    private ConditionsType type;
    @NotNull
    private OperationType operation;
    @NotNull
    private Integer value;
}
