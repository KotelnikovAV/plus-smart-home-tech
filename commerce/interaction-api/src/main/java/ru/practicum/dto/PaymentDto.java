package ru.practicum.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PaymentDto {
    @NotBlank
    private String paymentId;
    @NotNull
    private Double totalPayment;
    @NotNull
    private Double deliveryTotal;
    @NotNull
    private Double feeTotal;
}
