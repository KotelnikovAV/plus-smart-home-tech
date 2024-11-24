package ru.practicum.dto;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ChangeProductQuantityRequestDto {
    @NotNull
    private String productId;
    @NotNull
    private Integer newQuantity;
}
