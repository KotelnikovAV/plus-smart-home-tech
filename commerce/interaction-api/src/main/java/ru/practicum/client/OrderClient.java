package ru.practicum.client;

import jakarta.validation.Valid;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;
import ru.practicum.dto.CreateNewOrderRequestDto;
import ru.practicum.dto.OrderDto;
import ru.practicum.dto.ProductReturnRequestDto;
import ru.practicum.dto.ShippedToDeliveryRequestDto;
import ru.practicum.fallback.OrderFallback;

import java.util.List;

@FeignClient(name = "order", fallback = OrderFallback.class)
public interface OrderClient {

    @GetMapping("/api/v1/order")
    List<OrderDto> findByUsername(@RequestParam(required = false) String username,
                                  @RequestParam(defaultValue = "0") Integer page,
                                  @RequestParam(defaultValue = "1") Integer size,
                                  @RequestParam(required = false) List<String> sort,
                                  @RequestParam(defaultValue = "ASC") String sortOrder);

    @PutMapping("/api/v1/order")
    OrderDto saveOrder(@RequestBody @Valid CreateNewOrderRequestDto newOrder);

    @PostMapping("/api/v1/order/return")
    OrderDto returnOrder(@RequestBody @Valid ProductReturnRequestDto productReturn);

    @PostMapping("/api/v1/order/payment")
    OrderDto payingOrder(@RequestBody String orderId);

    @PostMapping("/api/v1/order/payment/failed")
     OrderDto rollbackPayment(@RequestBody String orderId);

    @PostMapping("/api/v1/order/delivery")
    OrderDto deliverOrder(@RequestBody String orderId);

    @PostMapping("/api/v1/order/delivery/failed")
    OrderDto rollbackDeliverOrder(@RequestBody String orderId);

    @PostMapping("/api/v1/order/completed")
    OrderDto completeOrder(@RequestBody String orderId);

    @PostMapping("/api/v1/order/calculate/total")
    OrderDto calculateCostOrder(@RequestBody String orderId);

    @PostMapping("/api/v1/order/calculate/delivery")
    OrderDto calculateCostDelivery(@RequestBody String orderId);

    @PostMapping("/api/v1/order/assembly")
    OrderDto assembleOrder(@RequestBody String orderId);

    @PostMapping("/api/v1/order/assembly/failed")
    OrderDto rollbackAssembleOrder(@RequestBody String orderId);

    @PostMapping("shipped")
    void recordDeliveryData(@RequestBody ShippedToDeliveryRequestDto delivery);
}
