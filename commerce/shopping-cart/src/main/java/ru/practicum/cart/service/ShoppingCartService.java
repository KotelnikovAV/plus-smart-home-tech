package ru.practicum.cart.service;

import ru.practicum.dto.BookedProductsDto;
import ru.practicum.dto.ChangeProductQuantityRequestDto;
import ru.practicum.dto.ShoppingCartDto;

import java.util.List;
import java.util.Map;

public interface ShoppingCartService {
    ShoppingCartDto findShoppingCart(String userName, String shoppingCartId);

    ShoppingCartDto saveShoppingCart(String userName, Map<String, Integer> products);

    void deleteShoppingCart(String userName);

    ShoppingCartDto updateShoppingCart(String userName, List<String> products);

    ShoppingCartDto changeProductQuantity(String userName, ChangeProductQuantityRequestDto quantity);

    BookedProductsDto bookingProducts(String userName);
}
