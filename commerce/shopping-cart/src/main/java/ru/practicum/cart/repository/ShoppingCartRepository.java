package ru.practicum.cart.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.practicum.cart.model.ShoppingCart;

import java.util.Optional;

public interface ShoppingCartRepository extends JpaRepository<ShoppingCart, String> {
    boolean existsByUserName(String userName);

    Optional<ShoppingCart> findByUserName(String userName);

    Optional<ShoppingCart> findByIdAndUserName(String id, String userName);

    void deleteByUserName(String userName);
}
