package ru.practicum.delivery.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.practicum.delivery.model.Delivery;

import java.util.Optional;

public interface DeliveryRepository extends JpaRepository<Delivery, String> {
    boolean existsByOrderId(String orderId);

    Optional<Delivery> findByOrderId(String orderId);
}
