package ru.practicum.events.model.hub;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import org.hibernate.validator.constraints.Length;
import ru.practicum.events.model.hub.enums.HubsEventType;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class ScenarioRemovedEvent extends HubEvent {
    @NotBlank
    @Length(min = 3)
    private String name;
    @NotNull
    private HubsEventType type;

    @Override
    public HubsEventType getType() {
        return HubsEventType.SCENARIO_REMOVED_EVENT;
    }
}
