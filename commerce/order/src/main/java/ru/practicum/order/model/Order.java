package ru.practicum.order.model;

import jakarta.persistence.*;
import lombok.*;
import ru.practicum.dto.OrderState;

import java.util.Map;

@Entity
@Table(name = "orders")
@Getter
@Setter
@EqualsAndHashCode(of = "id")
@NoArgsConstructor
@AllArgsConstructor
public class Order {
    @Id
    private String id;

    @Column
    private String shoppingCartId;

    @ElementCollection
    @CollectionTable(name = "order_cart_mapping",
            joinColumns = {@JoinColumn(name = "cart_id", referencedColumnName = "id")})
    @MapKeyColumn(name = "product_id")
    @Column(name = "quantity")
    private Map<String, Integer> products;

    @Column
    private String paymentId;

    @Column
    private String deliveryId;

    @Column
    @Enumerated(value = EnumType.STRING)
    private OrderState state;

    @Column
    private Double deliveryWeight;

    @Column
    private Double deliveryVolume;

    @Column
    private boolean fragile;

    @Column
    private Double totalPrice;

    @Column
    private Double deliveryPrice;

    @Column
    private Double productPrice;
}
