package ru.practicum.payment.service;

import ru.practicum.dto.OrderDto;
import ru.practicum.dto.PaymentDto;

public interface PaymentService {
    PaymentDto formationPayment(OrderDto order);

    Double calculationTotalCostOrder(OrderDto order);

    void payingOrder(String orderId);

    Double calculationCostProducts(OrderDto order);

    void rollbackPayingOrder(String orderId);
}
