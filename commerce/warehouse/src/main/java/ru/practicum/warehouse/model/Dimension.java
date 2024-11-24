package ru.practicum.warehouse.model;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "dimension")
@Getter
@Setter
@EqualsAndHashCode(of = "id")
@NoArgsConstructor
@AllArgsConstructor
public class Dimension {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column
    private Double width;

    @Column
    private Double height;

    @Column
    private Double depth;
}
