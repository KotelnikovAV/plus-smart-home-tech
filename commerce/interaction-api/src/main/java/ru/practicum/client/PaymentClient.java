package ru.practicum.client;

import jakarta.validation.Valid;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import ru.practicum.dto.OrderDto;
import ru.practicum.dto.PaymentDto;
import ru.practicum.fallback.PaymentFallback;

@FeignClient(name = "payment", fallback = PaymentFallback.class)
public interface PaymentClient {
    @PostMapping("/api/v1/payment")
    PaymentDto formationPayment(@RequestBody @Valid OrderDto order);

    @PostMapping("/api/v1/payment/totalCost")
    Double calculationTotalCostOrder(@RequestBody @Valid OrderDto order);

    @PostMapping("/api/v1/payment/refund")
    void payingOrder(@RequestBody String orderId);

    @PostMapping("/api/v1/payment/productCost")
    Double calculationCostProducts(@RequestBody @Valid OrderDto order);

    @PostMapping("/api/v1/payment/failed")
    void rollbackPayingOrder(@RequestBody String orderId);
}
