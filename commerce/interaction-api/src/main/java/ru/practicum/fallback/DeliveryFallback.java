package ru.practicum.fallback;

import org.springframework.stereotype.Component;
import ru.practicum.client.DeliveryClient;
import ru.practicum.dto.DeliveryDto;
import ru.practicum.dto.OrderDto;
import ru.practicum.exception.ServerUnavailableException;

@Component
public class DeliveryFallback implements DeliveryClient {
    @Override
    public DeliveryDto saveDelivery(DeliveryDto deliveryDto) {
        throw new ServerUnavailableException("Endpoint /api/v1/delivery method PUT is unavailable");
    }

    @Override
    public DeliveryDto findDelivery(String orderId) {
        throw new ServerUnavailableException("Endpoint /api/v1/delivery method GET is unavailable");
    }

    @Override
    public void makingSuccessfulDelivery(String orderId) {
        throw new ServerUnavailableException("Endpoint /api/v1/delivery/successful method POST is unavailable");
    }

    @Override
    public void makingInProgressDelivered(String orderId) {
        throw new ServerUnavailableException("Endpoint /api/v1/delivery/picked method POST is unavailable");
    }

    @Override
    public void makingFailedDelivery(String orderId) {
        throw new ServerUnavailableException("Endpoint /api/v1/delivery/failed method POST is unavailable");
    }

    @Override
    public Double calculationTotalCostDelivery(OrderDto orderDto) {
        throw new ServerUnavailableException("Endpoint /api/v1/delivery/cost method POST is unavailable");
    }
}
