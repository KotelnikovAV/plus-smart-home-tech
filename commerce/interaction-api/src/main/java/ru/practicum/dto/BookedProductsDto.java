package ru.practicum.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class BookedProductsDto {
    @NotNull
    @PositiveOrZero
    private Double deliveryWeight;
    @NotNull
    @PositiveOrZero
    private Double deliveryVolume;
    @NotNull
    private Boolean fragile;
}
