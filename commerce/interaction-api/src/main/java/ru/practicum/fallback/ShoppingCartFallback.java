package ru.practicum.fallback;

import org.springframework.stereotype.Component;
import ru.practicum.client.ShoppingCartClient;
import ru.practicum.dto.BookedProductsDto;
import ru.practicum.dto.ChangeProductQuantityRequestDto;
import ru.practicum.dto.ShoppingCartDto;
import ru.practicum.exception.ServerUnavailableException;

import java.util.List;
import java.util.Map;

@Component
public class ShoppingCartFallback implements ShoppingCartClient {

    @Override
    public ShoppingCartDto findShoppingCart(String username, String shoppingCartId) {
        throw new ServerUnavailableException("Endpoint /api/v1/shopping-cart method GET is unavailable");
    }

    @Override
    public ShoppingCartDto saveShoppingCart(String username, Map<String, Integer> products) {
        throw new ServerUnavailableException("Endpoint /api/v1/shopping-cart method PUT is unavailable");
    }

    @Override
    public void deleteShoppingCart(String username) {
        throw new ServerUnavailableException("Endpoint /api/v1/shopping-cart method DELETE is unavailable");
    }

    @Override
    public ShoppingCartDto updateShoppingCart(String username, List<String> products) {
        throw new ServerUnavailableException("Endpoint /api/v1/shopping-cart/remove method POST is unavailable");
    }

    @Override
    public ShoppingCartDto changeProductQuantity(String username, ChangeProductQuantityRequestDto quantity) {
        throw new ServerUnavailableException("Endpoint /api/v1/shopping-cart/change-quantity method POST is unavailable");
    }

    @Override
    public BookedProductsDto bookingProducts(String username) {
        throw new ServerUnavailableException("Endpoint /api/v1/shopping-cart/booking method POST is unavailable");
    }

    @Override
    public List<ShoppingCartDto> findShoppingCartsByUsername(String username) {
        throw new ServerUnavailableException("Endpoint /api/v1/shopping-cart/{username} method GET is unavailable");
    }
}
