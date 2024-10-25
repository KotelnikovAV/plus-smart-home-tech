package ru.practicum.events.model.hub;

import jakarta.validation.constraints.NotBlank;
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
    private HubsEventType type = HubsEventType.SCENARIO_REMOVED_EVENT;;

    @Override
    public HubsEventType getType() {
        return type;
    }
}
