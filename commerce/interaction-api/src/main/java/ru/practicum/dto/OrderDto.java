package ru.practicum.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Map;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class OrderDto {
    @NotBlank
    private String orderId;
    @NotBlank
    private String shoppingCartId;
    @NotNull
    private Map<String, Integer> products;
    private String paymentId;
    private String deliveryId;
    @NotNull
    private OrderState state;
    private Double deliveryWeight;
    private Double deliveryVolume;
    private boolean fragile;
    private Double totalPrice;
    private Double deliveryPrice;
    private Double productPrice;
}
