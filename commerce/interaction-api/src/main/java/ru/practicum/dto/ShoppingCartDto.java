package ru.practicum.dto;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Map;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ShoppingCartDto {
    private String shoppingCartId;
    @NotNull
    private Map<String, Integer> products;
}
