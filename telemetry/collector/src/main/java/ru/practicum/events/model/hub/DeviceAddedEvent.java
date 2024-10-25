package ru.practicum.events.model.hub;

import jakarta.validation.constraints.NotNull;
import lombok.*;
import ru.practicum.events.model.hub.enums.HubsEventType;
import ru.practicum.events.model.sensor.enums.SensorType;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class DeviceAddedEvent extends HubEvent {
    @NotNull
    private String id;
    @NotNull
    private SensorType deviceType;
    @NotNull
    private HubsEventType type = HubsEventType.DEVICE_ADDED_EVENT;

    @Override
    public HubsEventType getType() {
        return type;
    }
}
