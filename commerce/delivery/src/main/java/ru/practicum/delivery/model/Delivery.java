package ru.practicum.delivery.model;

import jakarta.persistence.*;
import lombok.*;
import ru.practicum.dto.DeliveryState;

@Entity
@Table(name = "delivery")
@Getter
@Setter
@EqualsAndHashCode(of = "deliveryId")
@NoArgsConstructor
@AllArgsConstructor
public class Delivery {
    @Id
    private String deliveryId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "from_address_id")
    private Address fromAddress;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "to_address_id")
    private Address toAddress;

    @Column
    private String orderId;

    @Column
    @Enumerated(value = EnumType.STRING)
    private DeliveryState deliveryState;
}
