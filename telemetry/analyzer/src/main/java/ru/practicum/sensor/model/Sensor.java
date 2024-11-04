package ru.practicum.sensor.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import ru.yandex.practicum.kafka.telemetry.event.SensorEventTypeAvro;

@Entity
@Table(name = "sensors")
@Getter
@Setter
@EqualsAndHashCode(of = "id")
@NoArgsConstructor
@AllArgsConstructor
public class Sensor {
    @Id
    @NotBlank
    String id;

    @NotBlank
    @Column
    String hubId;

    @NotNull
    @Column
    @Enumerated(value = EnumType.STRING)
    SensorEventTypeAvro type;
}
