package ru.practicum.order.service;

import ru.practicum.dto.CreateNewOrderRequestDto;
import ru.practicum.dto.OrderDto;
import ru.practicum.dto.ProductReturnRequestDto;

import java.util.List;

public interface OrderService {
    List<OrderDto> findByUsername(String username, Integer page, Integer size, List<String> sort, String order);

    OrderDto saveOrder(CreateNewOrderRequestDto newOrder);

    OrderDto returnOrder(ProductReturnRequestDto productReturn);

    OrderDto payingOrder(String orderId);

    OrderDto rollbackPayment(String orderId);

    OrderDto deliverOrder(String orderId);

    OrderDto rollbackDeliverOrder(String orderId);

    OrderDto completeOrder(String orderId);

    OrderDto calculateCostOrder(String orderId);

    OrderDto calculateCostDelivery(String orderId);

    OrderDto assembleOrder(String orderId);

    OrderDto rollbackAssembleOrder(String orderId);
}
