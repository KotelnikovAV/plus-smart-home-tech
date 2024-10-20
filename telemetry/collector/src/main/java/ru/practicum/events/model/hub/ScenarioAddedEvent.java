package ru.practicum.events.model.hub;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import org.hibernate.validator.constraints.Length;
import ru.practicum.events.model.hub.enums.HubsEventType;

import java.util.List;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class ScenarioAddedEvent extends HubEvent {
    @NotBlank
    @Length(min = 3)
    private String name;
    @NotNull
    private List<ScenarioCondition> conditions;
    @NotNull
    private List<DeviceAction> actions;
    @NotNull
    private HubsEventType type;

    @Override
    public HubsEventType getType() {
        return HubsEventType.SCENARIO_ADDED_EVENT;
    }
}
