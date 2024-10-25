package ru.practicum.events.model.hub;

import jakarta.validation.constraints.NotNull;
import lombok.*;
import ru.practicum.events.model.hub.enums.HubsEventType;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class DeviceRemovedEvent extends HubEvent {
    @NotNull
    private String id;
    private HubsEventType type = HubsEventType.DEVICE_REMOVED_EVENT;

    @Override
    public HubsEventType getType() {
        return type;
    }
}
