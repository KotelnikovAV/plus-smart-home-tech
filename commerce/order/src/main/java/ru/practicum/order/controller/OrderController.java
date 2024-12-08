package ru.practicum.order.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.client.OrderClient;
import ru.practicum.dto.CreateNewOrderRequestDto;
import ru.practicum.dto.OrderDto;
import ru.practicum.dto.ProductReturnRequestDto;
import ru.practicum.order.service.OrderService;

import java.util.List;

@RestController
@RequestMapping("api/v1/order")
@RequiredArgsConstructor
@Slf4j
@Validated
public class OrderController implements OrderClient {
    private final OrderService orderService;

    @GetMapping
    @Override
    public List<OrderDto> findByUsername(@RequestParam(required = false) String username,
                                         @RequestParam(defaultValue = "0") Integer page,
                                         @RequestParam(defaultValue = "1") Integer size,
                                         @RequestParam(required = false) List<String> sort,
                                         @RequestParam(defaultValue = "ASC") String sortOrder) {
        log.info("Finding orders by username {}", username);
        return orderService.findByUsername(username, page, size, sort, sortOrder);
    }

    @PutMapping
    @Override
    public OrderDto saveOrder(@RequestBody @Valid CreateNewOrderRequestDto newOrder) {
        log.info("Saving new order {}", newOrder);
        return orderService.saveOrder(newOrder);
    }

    @PostMapping("return")
    @Override
    public OrderDto returnOrder(@RequestBody @Valid ProductReturnRequestDto productReturn) {
        log.info("Returning order {}", productReturn);
        return orderService.returnOrder(productReturn);
    }

    @PostMapping("payment")
    @Override
    public OrderDto payingOrder(@RequestBody String orderId) {
        log.info("Paying order {}", orderId);
        return orderService.payingOrder(orderId);
    }

    @PostMapping("payment/failed")
    @Override
    public OrderDto rollbackPayment(@RequestBody String orderId) {
        log.info("Rolling back order {}", orderId);
        return orderService.rollbackPayment(orderId);
    }

    @PostMapping("delivery")
    @Override
    public OrderDto deliverOrder(@RequestBody String orderId) {
        log.info("Delivering order {}", orderId);
        return orderService.deliverOrder(orderId);
    }

    @PostMapping("delivery/failed")
    @Override
    public OrderDto rollbackDeliverOrder(@RequestBody String orderId) {
        log.info("Rolling back delivery order {}", orderId);
        return orderService.rollbackDeliverOrder(orderId);
    }

    @PostMapping("completed")
    @Override
    public OrderDto completeOrder(@RequestBody String orderId) {
        log.info("Completing order {}", orderId);
        return orderService.completeOrder(orderId);
    }

    @PostMapping("calculate/total")
    @Override
    public OrderDto calculateCostOrder(@RequestBody String orderId) {
        log.info("Calculating cost order {}", orderId);
        return orderService.calculateCostOrder(orderId);
    }

    @PostMapping("calculate/delivery")
    @Override
    public OrderDto calculateCostDelivery(@RequestBody String orderId) {
        log.info("Calculating delivery order {}", orderId);
        return orderService.calculateCostDelivery(orderId);
    }

    @PostMapping("assembly")
    @Override
    public OrderDto assembleOrder(@RequestBody String orderId) {
        log.info("Assembling order {}", orderId);
        return orderService.assembleOrder(orderId);
    }

    @PostMapping("assembly/failed")
    @Override
    public OrderDto rollbackAssembleOrder(@RequestBody String orderId) {
        log.info("Rolling back assembly order {}", orderId);
        return orderService.rollbackAssembleOrder(orderId);
    }
}
