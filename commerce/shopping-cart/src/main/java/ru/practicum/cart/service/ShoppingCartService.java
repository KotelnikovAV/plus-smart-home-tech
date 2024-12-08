package ru.practicum.cart.service;

import ru.practicum.dto.BookedProductsDto;
import ru.practicum.dto.ChangeProductQuantityRequestDto;
import ru.practicum.dto.ShoppingCartDto;

import java.util.List;
import java.util.Map;

public interface ShoppingCartService {
    ShoppingCartDto findShoppingCart(String username, String shoppingCartId);

    ShoppingCartDto saveShoppingCart(String username, Map<String, Integer> products);

    void deleteShoppingCart(String username);

    ShoppingCartDto updateShoppingCart(String username, List<String> products);

    ShoppingCartDto changeProductQuantity(String username, ChangeProductQuantityRequestDto quantity);

    BookedProductsDto bookingProducts(String username);

    List<ShoppingCartDto> findShoppingCartsIdByUsername(String username);
}
