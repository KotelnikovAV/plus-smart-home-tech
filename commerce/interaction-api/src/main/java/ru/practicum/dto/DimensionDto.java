package ru.practicum.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class DimensionDto {
    @NotNull
    @PositiveOrZero
    private Double width;
    @NotNull
    @PositiveOrZero
    private Double height;
    @NotNull
    @PositiveOrZero
    private Double depth;
}
