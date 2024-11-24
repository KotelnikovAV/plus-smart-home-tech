package ru.practicum.warehouse.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.*;

@Entity
@Table(name = "warehouse")
@Getter
@Setter
@EqualsAndHashCode(of = "productId")
@NoArgsConstructor
@AllArgsConstructor
public class Warehouse {
    @Id
    private String productId;

    @Column
    private boolean fragile;

    @NotNull
    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinColumn(name = "dimension_id")
    private Dimension dimension;

    @Column
    private Double weight;

    @Column
    private Integer quantity;
}
