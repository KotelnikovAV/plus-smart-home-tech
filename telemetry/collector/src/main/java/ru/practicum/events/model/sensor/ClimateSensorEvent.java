package ru.practicum.events.model.sensor;

import jakarta.validation.constraints.NotNull;
import lombok.*;
import ru.practicum.events.model.sensor.enums.SensorEventType;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class ClimateSensorEvent extends SensorEvent {
    @NotNull
    private Integer temperatureC;
    @NotNull
    private Integer humidity;
    @NotNull
    private Integer co2Level;
    private SensorEventType type = SensorEventType.CLIMATE_SENSOR_EVENT;

    @Override
    public SensorEventType getType() {
        return type;
    }
}
