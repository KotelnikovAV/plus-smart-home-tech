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
    @NotNull
    private HubsEventType type;

    @Override
    public HubsEventType getType() {
        return HubsEventType.DEVICE_REMOVED_EVENT;
    }
}
