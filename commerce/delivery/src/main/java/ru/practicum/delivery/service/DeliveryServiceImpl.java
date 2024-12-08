package ru.practicum.delivery.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.client.OrderClient;
import ru.practicum.delivery.exception.DuplicateDeliveryException;
import ru.practicum.delivery.exception.NoDeliveryFoundException;
import ru.practicum.delivery.exception.NotExistentWarehouseAddressException;
import ru.practicum.delivery.mapper.DeliveryMapper;
import ru.practicum.delivery.model.Address;
import ru.practicum.delivery.model.Delivery;
import ru.practicum.delivery.repository.DeliveryRepository;
import ru.practicum.dto.DeliveryDto;
import ru.practicum.dto.DeliveryState;
import ru.practicum.dto.OrderDto;
import ru.practicum.dto.ShippedToDeliveryRequestDto;

import java.util.UUID;

@RequiredArgsConstructor
@Service
@Slf4j
public class DeliveryServiceImpl implements DeliveryService {
    private static final String ADDRESS_1 = "country1city1street1house1flat1";  // абстрактные адреса
    private static final String ADDRESS_2 = "country2city2street2house2flat2";
    private static final Double BASIC_DELIVERY_COST = 5.0;
    private static final Double FRAGILITY_MULTIPLIER = 0.2;
    private static final Double WEIGHT_MULTIPLIER = 0.2;
    private static final Double VOLUME_MULTIPLIER = 0.2;
    private static final Double ADDRESS_MULTIPLIER = 0.2;

    private final DeliveryRepository deliveryRepository;
    private final DeliveryMapper deliveryMapper;
    private final OrderClient orderClient;

    @Override
    @Transactional
    public DeliveryDto saveDelivery(DeliveryDto deliveryDto) {
        log.info("Saving delivery: {}", deliveryDto);

        if (deliveryRepository.existsByOrderId(deliveryDto.getOrderId())) {
            throw new DuplicateDeliveryException("The delivery for this order already exists");
        }

        Delivery delivery = deliveryMapper.deliveryDtoToDelivery(deliveryDto);
        delivery.setDeliveryId(UUID.randomUUID().toString());
        delivery.setDeliveryState(DeliveryState.CREATED);
        delivery = deliveryRepository.save(delivery);

        orderClient.recordDeliveryData(new ShippedToDeliveryRequestDto(deliveryDto.getOrderId(),
                delivery.getDeliveryId()));

        log.info("Saved delivery: {}", delivery);

        return deliveryMapper.deliveryToDeliveryDto(delivery);
    }

    @Override
    @Transactional
    public void makingSuccessfulDelivery(String orderId) {
        log.info("Making a successful delivery for order: {}", orderId);

        Delivery delivery = findDeliveryByOrderId(orderId);
        delivery.setDeliveryState(DeliveryState.DELIVERED);
        log.info("Made a successful delivery for order: {}", orderId);
    }

    @Override
    public void makingInProgressDelivered(String orderId) {
        log.info("Making a in progress delivery for order: {}", orderId);

        Delivery delivery = findDeliveryByOrderId(orderId);
        delivery.setDeliveryState(DeliveryState.IN_PROGRESS);
        log.info("Made a in progress delivery for order: {}", orderId);
    }

    @Override
    @Transactional
    public void makingFailedDelivery(String orderId) {
        log.info("Making a failed delivery for order: {}", orderId);

        Delivery delivery = findDeliveryByOrderId(orderId);
        delivery.setDeliveryState(DeliveryState.FAILED);
        log.info("Made a failed delivery for order: {}", orderId);
    }

    @Override
    public Double calculationTotalCostDelivery(OrderDto orderDto) {
        log.info("Calculating total cost for order: {}", orderDto);

        Delivery delivery = deliveryRepository.findByOrderId(orderDto.getOrderId())
                .orElseThrow(() -> new NoDeliveryFoundException("There is no delivery for such an order"));

        double deliveryCost = BASIC_DELIVERY_COST * getMultiplierForWarehouseAddress(delivery.getFromAddress());

        if (orderDto.isFragile()) {
            deliveryCost = deliveryCost + deliveryCost * FRAGILITY_MULTIPLIER;
        }

        deliveryCost = deliveryCost + orderDto.getDeliveryWeight() * WEIGHT_MULTIPLIER;
        deliveryCost = deliveryCost + orderDto.getDeliveryVolume() * VOLUME_MULTIPLIER;

        if (!delivery.getFromAddress().getStreet().equals(delivery.getToAddress().getStreet())) {
            deliveryCost = deliveryCost + deliveryCost * ADDRESS_MULTIPLIER;
        }

        return deliveryCost;
    }

    @Override
    @Transactional(readOnly = true)
    public DeliveryDto findDelivery(String orderId) {
        log.info("Finding delivery for order: {}", orderId);

        Delivery delivery = findDeliveryByOrderId(orderId);
        log.info("Found delivery for order: {}", orderId);

        return deliveryMapper.deliveryToDeliveryDto(delivery);
    }

    private int getMultiplierForWarehouseAddress(Address addressDto) {
        String addressWarehouse = addressDto.getCountry() +
                addressDto.getCity() +
                addressDto.getStreet() +
                addressDto.getHouse() +
                addressDto.getFlat();
        if (addressWarehouse.equals(ADDRESS_1)) {
            return 1;
        } else if (addressWarehouse.equals(ADDRESS_2)) {
            return 2;
        } else {
            throw new NotExistentWarehouseAddressException("Non-existent warehouse address");
        }
    }

    private Delivery findDeliveryByOrderId(String orderId) {
        orderId = orderId.substring(1, orderId.length() - 1);

        return deliveryRepository.findByOrderId(orderId)
                .orElseThrow(() -> new NoDeliveryFoundException("There is no delivery for such an order"));
    }
}
