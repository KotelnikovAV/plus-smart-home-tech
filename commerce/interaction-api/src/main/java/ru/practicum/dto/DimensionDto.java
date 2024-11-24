package ru.practicum.dto;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class DimensionDto {
    @NotNull
    private Double width;
    @NotNull
    private Double height;
    @NotNull
    private Double depth;
}
