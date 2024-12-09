package ru.practicum.fallback;

import org.springframework.stereotype.Component;
import ru.practicum.client.WarehouseClient;
import ru.practicum.dto.*;
import ru.practicum.exception.ServerUnavailableException;

import java.util.Map;

@Component
public class WarehouseFallback implements WarehouseClient {
    @Override
    public NewProductInWarehouseRequestDto saveProduct(NewProductInWarehouseRequestDto newProduct) {
        throw new ServerUnavailableException("Endpoint /api/v1/warehouse method PUT is unavailable");
    }

    @Override
    public void returnProducts(Map<String, Integer> products) {
        throw new ServerUnavailableException("Endpoint /api/v1/warehouse/return method POST is unavailable");
    }

    @Override
    public BookedProductsDto checkProducts(ShoppingCartDto shoppingCart) {
        throw new ServerUnavailableException("Endpoint /api/v1/warehouse/check method POST is unavailable");
    }

    @Override
    public BookedProductsDto assembleProducts(AssemblyProductForOrderFromShoppingCartRequestDto assembly) {
        throw new ServerUnavailableException("Endpoint /api/v1/warehouse/assembly method POST is unavailable");
    }

    @Override
    public AddProductToWarehouseRequestDto addProduct(AddProductToWarehouseRequestDto addProduct) {
        throw new ServerUnavailableException("Endpoint /api/v1/warehouse/add method POST is unavailable");
    }

    @Override
    public AddressDto getAddress() {
        throw new ServerUnavailableException("Endpoint /api/v1/warehouse/address method GET is unavailable");
    }
}
