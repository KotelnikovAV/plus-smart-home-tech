package ru.practicum.dto;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AssemblyProductForOrderFromShoppingCartRequestDto {
    @NotNull
    private String shoppingCartId;
    @NotNull
    private Long orderId;
}
