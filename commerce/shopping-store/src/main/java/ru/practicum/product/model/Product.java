package ru.practicum.product.model;

import jakarta.persistence.*;
import lombok.*;
import ru.practicum.product.dto.ProductCategory;
import ru.practicum.product.dto.ProductState;
import ru.practicum.product.dto.QuantityState;

@Entity
@Table(name = "products")
@Getter
@Setter
@EqualsAndHashCode(of = "productId")
@NoArgsConstructor
@AllArgsConstructor
public class Product {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long productId;

    @Column
    private String productName;

    @Column
    private String description;

    @Column
    private String imageSrc;

    @Column
    @Enumerated(value = EnumType.STRING)
    private QuantityState quantityState;

    @Column
    @Enumerated(value = EnumType.STRING)
    private ProductState productState;

    @Column
    private Double rating;

    @Column
    @Enumerated(value = EnumType.STRING)
    private ProductCategory productCategory;

    @Column
    private Double price;
}
