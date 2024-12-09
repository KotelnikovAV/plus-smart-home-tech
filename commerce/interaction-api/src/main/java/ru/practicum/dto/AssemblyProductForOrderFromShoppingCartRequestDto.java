package ru.practicum.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AssemblyProductForOrderFromShoppingCartRequestDto {
    @NotBlank
    private String shoppingCartId;
    @NotBlank
    private String orderId;
}
