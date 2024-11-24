package ru.practicum.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class NewProductInWarehouseRequestDto {
    @NotNull
    private String productId;
    private boolean fragile;
    @NotNull
    private DimensionDto dimension;
    @NotNull
    @Min(value = 1)
    private Double weight;
}
