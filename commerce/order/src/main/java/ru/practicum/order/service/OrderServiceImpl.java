package ru.practicum.order.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import ru.practicum.client.*;
import ru.practicum.dto.*;
import ru.practicum.order.exception.NoOrderFoundException;
import ru.practicum.order.exception.NotAuthorizedUserException;
import ru.practicum.order.mapper.OrderMapper;
import ru.practicum.order.model.Order;
import ru.practicum.order.repository.OrderRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
@Slf4j
public class OrderServiceImpl implements OrderService {
    private final OrderRepository orderRepository;
    private final OrderMapper orderMapper;
    private final ShoppingCartClient shoppingCartClient;
    private final ShoppingStoreClient shoppingStoreClient;
    private final WarehouseClient warehouseClient;
    private final PaymentClient paymentClient;
    private final DeliveryClient deliveryClient;

    @Transactional(readOnly = true)
    @Override
    public List<OrderDto> findByUsername(String username, Integer page, Integer size, List<String> sort, String order) {
        log.info("Finding orders");

        if (username == null || username.isEmpty()) {
            throw new NotAuthorizedUserException("The user is not logged in");
        }

        List<String> shoppingCartIds = getListShoppingCartId(username);
        Page<Order> products = orderRepository.findByShoppingCartIdIn(shoppingCartIds,
                getPageRequest(page, size, sort, order));
        List<Order> orders = products.getContent();

        if (CollectionUtils.isEmpty(orders)) {
            throw new NoOrderFoundException("The basket is empty");
        }

        log.info("Found {} orders", orders.size());
        return orderMapper.ordersToOrdersDto(orders);
    }

    @Transactional
    @Override
    public OrderDto saveOrder(CreateNewOrderRequestDto newOrder) {
        log.info("Saving new order");

        Order order = createOrder(newOrder.getShoppingCart());
        order = orderRepository.save(order);

        log.info("Saved order: {}", order);
        return orderMapper.orderToOrderDto(order);
    }

    @Transactional
    @Override
    public OrderDto returnOrder(ProductReturnRequestDto productReturn) {
        log.info("Returning order");

        Order order = orderRepository.findById(productReturn.getOrderId())
                .orElseThrow(() -> new NoOrderFoundException("The order does not exist"));
        order.setState(OrderState.CANCELED);

        warehouseClient.returnProducts(productReturn.getProducts());

        log.info("Returned order: {}", order);
        return orderMapper.orderToOrderDto(order);
    }

    @Transactional
    @Override
    public OrderDto payingOrder(String orderId) {
        log.info("Paying order");

        Order order = findOrderByOrderId(orderId);

        PaymentDto payment = paymentClient.formationPayment(orderMapper.orderToOrderDto(order));

        order.setPaymentId(payment.getPaymentId());
        order.setTotalPrice(payment.getTotalPayment());
        order.setDeliveryPrice(payment.getDeliveryTotal());
        order.setState(OrderState.ON_PAYMENT);
        log.info("Paying order: {}", order);

        return orderMapper.orderToOrderDto(order);
    }

    @Transactional
    @Override
    public OrderDto rollbackPayment(String orderId) {
        log.info("Rolling back payment order");

        Order order = findOrderByOrderId(orderId);

        paymentClient.rollbackPayingOrder(order.getId());
        order.setState(OrderState.PAYMENT_FAILED);
        log.info("Rolling back payment order: {}", order);

        return orderMapper.orderToOrderDto(order);
    }

    @Transactional
    @Override
    public OrderDto deliverOrder(String orderId) {
        log.info("Delivering order");

        Order order = findOrderByOrderId(orderId);
        order.setState(OrderState.ON_DELIVERY);
        log.info("Delivering order: {}", order);

        return orderMapper.orderToOrderDto(order);
    }

    @Transactional
    @Override
    public OrderDto rollbackDeliverOrder(String orderId) {
        log.info("Rolling back delivery order");

        Order order = findOrderByOrderId(orderId);
        order.setState(OrderState.DELIVERY_FAILED);

        deliveryClient.makingFailedDelivery(order.getId());

        log.info("Rolling back delivery order: {}", order);

        return orderMapper.orderToOrderDto(order);
    }

    @Transactional
    @Override
    public OrderDto completeOrder(String orderId) {
        log.info("Completing order");

        Order order = findOrderByOrderId(orderId);
        order.setState(OrderState.COMPLETED);

        log.info("Completed order: {}", order);
        return orderMapper.orderToOrderDto(order);
    }

    @Transactional
    @Override
    public OrderDto calculateCostOrder(String orderId) {
        log.info("Calculating cost order");

        Order order = findOrderByOrderId(orderId);

        double totalPrice = paymentClient.calculationTotalCostOrder(orderMapper.orderToOrderDto(order));

        order.setTotalPrice(totalPrice);
        log.info("Calculated cost order: {}", order);

        return orderMapper.orderToOrderDto(order);
    }

    @Transactional
    @Override
    public OrderDto calculateCostDelivery(String orderId) {
        log.info("Calculating cost delivery");

        Order order = findOrderByOrderId(orderId);

        double deliveryPrice = deliveryClient.calculationTotalCostDelivery(orderMapper.orderToOrderDto(order));

        order.setTotalPrice(deliveryPrice);
        log.info("Calculated cost delivery: {}", order);

        return orderMapper.orderToOrderDto(order);
    }

    @Transactional
    @Override
    public OrderDto assembleOrder(String orderId) {
        log.info("Assembling order");

        Order order = findOrderByOrderId(orderId);
        BookedProductsDto bookedProducts = warehouseClient
                .assembleProducts(new AssemblyProductForOrderFromShoppingCartRequestDto(
                order.getShoppingCartId(),
                null));
        order.setDeliveryWeight(bookedProducts.getDeliveryWeight());
        order.setDeliveryVolume(bookedProducts.getDeliveryVolume());
        order.setFragile(bookedProducts.getFragile());
        order.setState(OrderState.ASSEMBLED);

        log.info("Assembled order: {}", order);
        return orderMapper.orderToOrderDto(order);
    }

    @Transactional
    @Override
    public OrderDto rollbackAssembleOrder(String orderId) {
        log.info("Rolling back assembly order");

        Order order = findOrderByOrderId(orderId);
        order.setState(OrderState.ASSEMBLY_FAILED);
        order.setDeliveryWeight(0.0);
        order.setDeliveryVolume(0.0);
        order.setFragile(false);

        warehouseClient.returnProducts(order.getProducts());

        log.info("Rolling back assembly order: {}", order);

        return orderMapper.orderToOrderDto(order);
    }

    @Transactional
    @Override
    public void recordDeliveryData(ShippedToDeliveryRequestDto delivery) {
        log.info("Recording delivery data");

        Order order = orderRepository.findById(delivery.getOrderId())
                .orElseThrow(() -> new NoOrderFoundException("The order does not exist"));
        order.setState(OrderState.ON_DELIVERY);
        order.setDeliveryId(delivery.getDeliveryId());
        log.info("Recording delivery data: {}", order);
    }

    private PageRequest getPageRequest(Integer page, Integer size, List<String> sort, String sortOrder) {
        PageRequest pageRequest;

        if (CollectionUtils.isEmpty(sort)) {
            pageRequest = PageRequest.of(page, size, Sort.by(Sort.Direction.valueOf(sortOrder),
                    "orderId"));
        } else {
            List<Sort.Order> orders = new ArrayList<>();

            for (String s : sort) {
                orders.add(new Sort.Order(Sort.Direction.valueOf(sortOrder), s));
            }

            Sort sorts = Sort.by(orders);
            pageRequest = PageRequest.of(page, size, sorts);
        }
        return pageRequest;
    }

    private List<String> getListShoppingCartId(String username) {
        List<ShoppingCartDto> shoppingCarts = shoppingCartClient.findShoppingCartsByUsername(username);

        if (CollectionUtils.isEmpty(shoppingCarts)) {
            throw new NoOrderFoundException("The basket is empty");
        }

        return shoppingCarts.stream()
                .map(ShoppingCartDto::getShoppingCartId)
                .collect(Collectors.toList());
    }

    private Order createOrder(ShoppingCartDto shoppingCart) {
        double priceProduct = shoppingStoreClient.findPrice(shoppingCart.getProducts());

        Order order = new Order();
        order.setShoppingCartId(shoppingCart.getShoppingCartId());
        order.setProducts(shoppingCart.getProducts());
        order.setPaymentId("0");
        order.setDeliveryId("0");
        order.setState(OrderState.NEW);
        order.setProductPrice(priceProduct);
        order.setTotalPrice(0.0);
        order.setDeliveryWeight(0.0);
        order.setDeliveryVolume(0.0);
        order.setFragile(false);
        order.setDeliveryPrice(0.0);

        return order;
    }

    private Order findOrderByOrderId(String orderId) {
        if (orderId == null || orderId.isEmpty()) {
            throw new NoOrderFoundException("orderId is empty");
        }

        orderId = orderId.substring(1, orderId.length() - 1);
        return orderRepository.findById(orderId)
                .orElseThrow(() -> new NoOrderFoundException("The order does not exist"));
    }
}
