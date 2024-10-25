package ru.practicum.events.model.sensor;

import jakarta.validation.constraints.NotNull;
import lombok.*;
import ru.practicum.events.model.sensor.enums.SensorEventType;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class LightSensorEvent extends SensorEvent {
    @NotNull
    private Integer linkQuality;
    @NotNull
    private Integer luminosity;
    private SensorEventType type = SensorEventType.LIGHT_SENSOR_EVENT;

    @Override
    public SensorEventType getType() {
        return type;
    }
}
