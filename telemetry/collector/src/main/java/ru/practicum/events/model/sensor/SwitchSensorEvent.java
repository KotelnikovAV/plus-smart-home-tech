package ru.practicum.events.model.sensor;

import jakarta.validation.constraints.NotNull;
import lombok.*;
import ru.practicum.events.model.sensor.enums.SensorEventType;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class SwitchSensorEvent extends SensorEvent {
    @NotNull
    private Boolean state;
    @NotNull
    private SensorEventType type;

    @Override
    public SensorEventType getType() {
        return SensorEventType.SWITCH_SENSOR_EVENT;
    }
}
