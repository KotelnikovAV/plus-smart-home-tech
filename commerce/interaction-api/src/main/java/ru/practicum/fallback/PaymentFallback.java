package ru.practicum.fallback;

import org.springframework.stereotype.Component;
import ru.practicum.client.PaymentClient;
import ru.practicum.dto.OrderDto;
import ru.practicum.dto.PaymentDto;
import ru.practicum.exception.ServerUnavailableException;

@Component
public class PaymentFallback implements PaymentClient {
    @Override
    public PaymentDto formationPayment(OrderDto order) {
        throw new ServerUnavailableException("Endpoint /api/v1/payment method POST is unavailable");
    }

    @Override
    public Double calculationTotalCostOrder(OrderDto order) {
        throw new ServerUnavailableException("Endpoint /api/v1/payment/totalCost method POST is unavailable");
    }

    @Override
    public void payingOrder(String orderId) {
        throw new ServerUnavailableException("Endpoint /api/v1/payment/refund method POST is unavailable");
    }

    @Override
    public Double calculationCostProducts(OrderDto order) {
        throw new ServerUnavailableException("Endpoint /api/v1/payment/productCost method POST is unavailable");
    }

    @Override
    public void rollbackPayingOrder(String orderId) {
        throw new ServerUnavailableException("Endpoint /api/v1/payment/failed method POST is unavailable");
    }
}
