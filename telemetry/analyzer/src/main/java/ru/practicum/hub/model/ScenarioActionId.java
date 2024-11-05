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
public class ScenarioActionId implements Serializable {
    @NotNull
    @ManyToOne
    @JoinColumn(name = "scenario_id")
    Scenario scenario;

    @NotNull
    @ManyToOne
    @JoinColumn(name = "action_id")
    Action action;
}