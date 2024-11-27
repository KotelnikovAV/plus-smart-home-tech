package ru.practicum.product.model;

import jakarta.persistence.*;
import lombok.*;
import ru.practicum.dto.ProductCategory;
import ru.practicum.dto.ProductState;
import ru.practicum.dto.QuantityState;

@Entity
@Table(name = "products")
@Getter
@Setter
@EqualsAndHashCode(of = "productId")
@NoArgsConstructor
@AllArgsConstructor
public class Product {
    @Id
    private String productId;

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
