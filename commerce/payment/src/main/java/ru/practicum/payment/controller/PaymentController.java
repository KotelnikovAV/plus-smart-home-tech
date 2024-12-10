package ru.practicum.payment.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.practicum.client.PaymentClient;
import ru.practicum.dto.OrderDto;
import ru.practicum.dto.PaymentDto;
import ru.practicum.payment.service.PaymentService;

@RestController
@RequestMapping("api/v1/payment")
@RequiredArgsConstructor
@Slf4j
@Validated
public class PaymentController implements PaymentClient {
    private final PaymentService paymentService;

    @PostMapping
    @Override
    public PaymentDto formationPayment(@RequestBody @Valid OrderDto order) {
        log.info("Formating payment for order {}", order);
        return paymentService.formationPayment(order);
    }

    @PostMapping("totalCost")
    @Override
    public Double calculationTotalCostOrder(@RequestBody @Valid OrderDto order) {
        log.info("Calculating total cost for order {}", order);
        return paymentService.calculationTotalCostOrder(order);
    }

    @PostMapping("refund")
    @Override
    public void payingOrder(@RequestBody String orderId) {
        log.info("Paying order {}", orderId);
        paymentService.payingOrder(orderId);
    }

    @PostMapping("productCost")
    @Override
    public Double calculationCostProducts(@RequestBody @Valid OrderDto order) {
        log.info("Calculating cost for order {}", order);
        return paymentService.calculationCostProducts(order);
    }

    @PostMapping("failed")
    @Override
    public void rollbackPayingOrder(@RequestBody String orderId) {
        log.info("Rolling back order {}", orderId);
        paymentService.rollbackPayingOrder(orderId);
    }
}
