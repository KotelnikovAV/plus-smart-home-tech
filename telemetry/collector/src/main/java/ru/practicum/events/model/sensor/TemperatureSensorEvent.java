package ru.practicum.events.model.sensor;

import jakarta.validation.constraints.NotNull;
import lombok.*;
import ru.practicum.events.model.sensor.enums.SensorEventType;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class TemperatureSensorEvent extends SensorEvent {
    @NotNull
    private Integer temperatureC;
    @NotNull
    private Integer temperatureF;
    private SensorEventType type = SensorEventType.TEMPERATURE_SENSOR_EVENT;

    @Override
    public SensorEventType getType() {
        return type;
    }
}
