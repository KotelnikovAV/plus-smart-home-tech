package ru.practicum.fallback;

import org.springframework.stereotype.Component;
import ru.practicum.client.OrderClient;
import ru.practicum.dto.CreateNewOrderRequestDto;
import ru.practicum.dto.OrderDto;
import ru.practicum.dto.ProductReturnRequestDto;
import ru.practicum.dto.ShippedToDeliveryRequestDto;
import ru.practicum.exception.ServerUnavailableException;

import java.util.List;

@Component
public class OrderFallback implements OrderClient {

    @Override
    public List<OrderDto> findByUsername(String username, Integer page, Integer size, List<String> sort, String sortOrder) {
        throw new ServerUnavailableException("Endpoint /api/v1/order method GET is unavailable");
    }

    @Override
    public OrderDto saveOrder(CreateNewOrderRequestDto newOrder) {
        throw new ServerUnavailableException("Endpoint /api/v1/order method PUT is unavailable");
    }

    @Override
    public OrderDto returnOrder(ProductReturnRequestDto productReturn) {
        throw new ServerUnavailableException("Endpoint /api/v1/order/return method POST is unavailable");
    }

    @Override
    public OrderDto payingOrder(String orderId) {
        throw new ServerUnavailableException("Endpoint /api/v1/order/payment method POST is unavailable");
    }

    @Override
    public OrderDto rollbackPayment(String orderId) {
        throw new ServerUnavailableException("Endpoint /api/v1/order/payment/failed method POST is unavailable");
    }

    @Override
    public OrderDto deliverOrder(String orderId) {
        throw new ServerUnavailableException("Endpoint /api/v1/order/delivery method POST is unavailable");
    }

    @Override
    public OrderDto rollbackDeliverOrder(String orderId) {
        throw new ServerUnavailableException("Endpoint /api/v1/order/delivery/failed method POST is unavailable");
    }

    @Override
    public OrderDto completeOrder(String orderId) {
        throw new ServerUnavailableException("Endpoint /api/v1/order/completed method POST is unavailable");
    }

    @Override
    public OrderDto calculateCostOrder(String orderId) {
        throw new ServerUnavailableException("Endpoint /api/v1/order/calculate/total method POST is unavailable");
    }

    @Override
    public OrderDto calculateCostDelivery(String orderId) {
        throw new ServerUnavailableException("Endpoint /api/v1/order/calculate/delivery method POST is unavailable");
    }

    @Override
    public OrderDto assembleOrder(String orderId) {
        throw new ServerUnavailableException("Endpoint /api/v1/order/assembly method POST is unavailable");
    }

    @Override
    public OrderDto rollbackAssembleOrder(String orderId) {
        throw new ServerUnavailableException("Endpoint /api/v1/order/assembly/failed method GET is unavailable");
    }

    @Override
    public void recordDeliveryData(ShippedToDeliveryRequestDto delivery) {
        throw new ServerUnavailableException("Endpoint /api/v1/order/assembly/shipped method POST is unavailable");
    }
}
