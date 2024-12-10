package ru.practicum.delivery.service;

import ru.practicum.dto.DeliveryDto;
import ru.practicum.dto.OrderDto;

public interface DeliveryService {
    DeliveryDto saveDelivery(DeliveryDto deliveryDto);

    void makingSuccessfulDelivery(String orderId);

    void makingInProgressDelivered(String orderId);

    void makingFailedDelivery(String orderId);

    Double calculationTotalCostDelivery(OrderDto orderDto);

    DeliveryDto findDelivery(String orderId);
}
