package ru.practicum.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ShippedToDeliveryRequestDto {
    @NotBlank
    private String orderId;
    @NotBlank
    private String deliveryId;
}
