package ru.practicum.order.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;
import ru.practicum.dto.OrderDto;
import ru.practicum.order.model.Order;

import java.util.List;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface OrderMapper {
    @Mapping(target = "orderId", source = "id")
    OrderDto orderToOrderDto(Order order);

    List<OrderDto> ordersToOrdersDto(List<Order> orders);
}
