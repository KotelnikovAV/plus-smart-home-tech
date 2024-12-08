package ru.practicum.payment.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import ru.practicum.client.DeliveryClient;
import ru.practicum.client.ShoppingStoreClient;
import ru.practicum.dto.OrderDto;
import ru.practicum.dto.PaymentDto;
import ru.practicum.payment.exception.NoPaymentFoundException;
import ru.practicum.payment.exception.NotEnoughInfoInOrderToCalculateException;
import ru.practicum.payment.mapper.PaymentMapper;
import ru.practicum.payment.model.Payment;
import ru.practicum.payment.model.PaymentStatus;
import ru.practicum.payment.repository.PaymentRepository;

@RequiredArgsConstructor
@Service
@Slf4j
public class PaymentServiceImpl implements PaymentService {
    private static final Double TAX_MULTIPLIER = 0.1;

    private final PaymentRepository paymentRepository;
    private final PaymentMapper paymentMapper;
    private final ShoppingStoreClient shoppingStoreClient;
    private final DeliveryClient deliveryClient;

    @Override
    @Transactional
    public PaymentDto formationPayment(OrderDto order) {
        log.info("Formating payment for order {}", order);

        double priceProduct = calculationCostProducts(order);
        double priceDelivery = deliveryClient.calculationTotalCostDelivery(order);
        double tax = (priceProduct + priceDelivery) * TAX_MULTIPLIER;

        Payment payment = new Payment();
        payment.setTotalPayment(priceProduct + priceDelivery + tax);
        payment.setDeliveryTotal(priceDelivery);
        payment.setFeeTotal(tax);
        payment.setPaymentStatus(PaymentStatus.PENDING);

        payment = paymentRepository.save(payment);
        log.info("A payment has been formed {}", payment);

        return paymentMapper.paymentToPaymentDto(payment);
    }

    @Override
    public Double calculationTotalCostOrder(OrderDto order) {
        log.info("Calculating total cost order for order {}", order);

        double priceProduct = calculationCostProducts(order);
        double priceDelivery = deliveryClient.calculationTotalCostDelivery(order);
        double tax = (priceProduct + priceDelivery) * TAX_MULTIPLIER;
        log.info("Total cost order has been calculated {}", priceProduct + priceDelivery + tax);

        return priceProduct + priceDelivery + tax;
    }

    @Override
    @Transactional
    public void payingOrder(String orderId) {
        log.info("Paying order {}", orderId);

        if (checkValidOrderId(orderId)) {
            throw new NotEnoughInfoInOrderToCalculateException("The order id must not be empty");
        }

        orderId = orderId.substring(1, orderId.length() - 1);
        Payment payment = paymentRepository.findById(orderId)
                .orElseThrow(() -> new NoPaymentFoundException("The order does not exist"));
        payment.setPaymentStatus(PaymentStatus.SUCCESS);
        log.info("The payment was successful");
    }

    @Override
    public Double calculationCostProducts(OrderDto order) {
        log.info("calculation cost products");

        if (CollectionUtils.isEmpty(order.getProducts())) {
            throw new NotEnoughInfoInOrderToCalculateException("There are no products in the order");
        }

        Double price = shoppingStoreClient.findPrice(order.getProducts());
        log.info("calculation cost products price: {}", price);

        return price;
    }

    @Override
    @Transactional
    public void rollbackPayingOrder(String orderId) {
        log.info("Rolling back order {}", orderId);

        if (checkValidOrderId(orderId)) {
            throw new NotEnoughInfoInOrderToCalculateException("The order id must not be empty");
        }

        orderId = orderId.substring(1, orderId.length() - 1);
        Payment payment = paymentRepository.findById(orderId)
                .orElseThrow(() -> new NoPaymentFoundException("The order does not exist"));
        payment.setPaymentStatus(PaymentStatus.FAILED);
        log.info("The payment was rolled back");
    }

    private boolean checkValidOrderId(String orderId) {
        return orderId == null || orderId.isEmpty();
    }
}
