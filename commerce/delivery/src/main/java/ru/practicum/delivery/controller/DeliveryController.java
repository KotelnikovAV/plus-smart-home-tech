package ru.practicum.delivery.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.client.DeliveryClient;
import ru.practicum.delivery.service.DeliveryService;
import ru.practicum.dto.DeliveryDto;
import ru.practicum.dto.OrderDto;

@RestController
@RequestMapping("api/v1/delivery")
@RequiredArgsConstructor
@Slf4j
@Validated
public class DeliveryController implements DeliveryClient {
    private final DeliveryService deliveryService;

    @PutMapping
    @Override
    public DeliveryDto saveDelivery(@RequestBody @Valid DeliveryDto deliveryDto) {
        log.info("Saving delivery: {}", deliveryDto);
        return deliveryService.saveDelivery(deliveryDto);
    }

    @GetMapping
    @Override
    public DeliveryDto findDelivery(@RequestBody String orderId) {
        log.info("Finding delivery: {}", orderId);
        return deliveryService.findDelivery(orderId);
    }

    @PostMapping("successful")
    @Override
    public void makingSuccessfulDelivery(@RequestBody String orderId) {
        log.info("Successful delivery: {}", orderId);
        deliveryService.makingSuccessfulDelivery(orderId);
    }

    @PostMapping("picked")
    @Override
    public void makingInProgressDelivered(@RequestBody String orderId) {
        log.info("Picked delivery: {}", orderId);
        deliveryService.makingInProgressDelivered(orderId);
    }

    @PostMapping("failed")
    @Override
    public void makingFailedDelivery(@RequestBody String orderId) {
        log.info("Failed delivery: {}", orderId);
        deliveryService.makingFailedDelivery(orderId);
    }

    @PostMapping("cost")
    @Override
    public Double calculationTotalCostDelivery(@RequestBody OrderDto orderDto) {
        log.info("Calculating total cost for delivery: {}", orderDto);
        return deliveryService.calculationTotalCostDelivery(orderDto);
    }
}
