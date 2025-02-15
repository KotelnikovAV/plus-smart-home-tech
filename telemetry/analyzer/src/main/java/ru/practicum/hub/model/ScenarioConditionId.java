package ru.practicum.hub.model;

import jakarta.persistence.Embeddable;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.io.Serializable;

@Getter
@Setter
@EqualsAndHashCode
@NoArgsConstructor
@AllArgsConstructor
@Embeddable
public class ScenarioConditionId implements Serializable {
    @NotNull
    @ManyToOne
    @JoinColumn(name = "scenario_id")
    private Scenario scenario;

    @NotNull
    @ManyToOne
    @JoinColumn(name = "condition_id")
    private Condition condition;
}
