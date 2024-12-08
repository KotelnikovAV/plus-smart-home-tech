package ru.practicum.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import ru.practicum.dto.DeliveryDto;
import ru.practicum.dto.OrderDto;
import ru.practicum.fallback.DeliveryFallback;

@FeignClient(name = "delivery", fallback = DeliveryFallback.class)
public interface DeliveryClient {
    @PutMapping("/api/v1/delivery")
    DeliveryDto saveDelivery(DeliveryDto deliveryDto);

    @GetMapping
    DeliveryDto findDelivery(@RequestBody String orderId);

    @PostMapping("/api/v1/delivery/successful")
    void makingSuccessfulDelivery(String orderId);

    @PostMapping("/api/v1/delivery/picked")
    void makingInProgressDelivered(String orderId);

    @PostMapping("/api/v1/delivery/failed")
    void makingFailedDelivery(String orderId);

    @PostMapping("/api/v1/delivery/cost")
    Double calculationTotalCostDelivery(OrderDto orderDto);
}
