package ru.practicum.cart.model;

import jakarta.persistence.*;
import lombok.*;

import java.util.Map;

@Entity
@Table(name = "shopping_cart")
@Getter
@Setter
@EqualsAndHashCode(of = "id")
@NoArgsConstructor
@AllArgsConstructor
public class ShoppingCart {
    @Id
    private String id;

    @Column(name = "user_name")
    private String userName;

    @ElementCollection
    @CollectionTable(name = "shopping_cart_mapping",
            joinColumns = {@JoinColumn(name = "cart_id", referencedColumnName = "id")})
    @MapKeyColumn(name = "product_id")
    @Column(name = "quantity")
    private Map<String, Integer> products;
}
