package ru.practicum.order.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import ru.practicum.order.model.Order;

import java.util.List;

public interface OrderRepository extends JpaRepository<Order, String> {
    Page<Order> findByShoppingCartIdIn(List<String> shoppingCartId , Pageable pageable);
}
