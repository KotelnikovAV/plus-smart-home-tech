package ru.practicum.dto;

import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ProductDto {
    private String productId;
    @NotBlank
    private String productName;
    @NotBlank
    private String description;
    private String imageSrc;
    @NotNull
    private QuantityState quantityState;
    @NotNull
    private ProductState productState;
    @Min(value = 1)
    @Max(value = 5)
    private Double rating;
    @NotNull
    private ProductCategory productCategory;
    @NotNull
    @PositiveOrZero
    private Double price;
}
