package ru.practicum.sensor.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import ru.yandex.practicum.kafka.telemetry.event.DeviceTypeAvro;

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
    private String id;

    @NotBlank
    @Column
    private String hubId;

    @NotNull
    @Column
    @Enumerated(value = EnumType.STRING)
    private DeviceTypeAvro type;
}