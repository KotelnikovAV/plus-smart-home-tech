package ru.practicum.hub.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import ru.practicum.sensor.model.Sensor;
import ru.yandex.practicum.kafka.telemetry.event.ConditionOperationAvro;
import ru.yandex.practicum.kafka.telemetry.event.ConditionTypeAvro;

@Entity
@Table(name = "conditions")
@Getter
@Setter
@EqualsAndHashCode(of = "id")
@NoArgsConstructor
@AllArgsConstructor
public class Condition {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @ManyToOne
    @JoinColumn(name = "sensor_id")
    private Sensor sensor;

    @NotNull
    @Column
    @Enumerated(value = EnumType.STRING)
    private ConditionTypeAvro type;

    @NotNull
    @Column
    @Enumerated(value = EnumType.STRING)
    private ConditionOperationAvro operation;

    @NotNull
    @Column
    private Integer amount;
}
