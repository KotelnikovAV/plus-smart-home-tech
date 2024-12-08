package ru.practicum.cart.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.practicum.cart.model.ShoppingCart;

import java.util.List;
import java.util.Optional;

public interface ShoppingCartRepository extends JpaRepository<ShoppingCart, String> {
    boolean existsByUsername(String userName);

    Optional<ShoppingCart> findByUsernameAndActive(String username, boolean active);

    Optional<ShoppingCart> findByIdAndUsername(String id, String userName);

    void deleteByUsernameAndActive(String userName, boolean active);

    List<ShoppingCart> findByUsername(String userName);
}