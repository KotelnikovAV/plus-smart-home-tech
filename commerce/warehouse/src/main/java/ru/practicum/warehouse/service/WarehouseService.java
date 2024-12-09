package ru.practicum.warehouse.service;

import ru.practicum.dto.*;

import java.util.Map;

public interface WarehouseService {
    NewProductInWarehouseRequestDto saveProduct(NewProductInWarehouseRequestDto newProductInWarehouseRequestDto);

    void returnProducts(Map<String, Integer> returnProducts);

    BookedProductsDto checkProducts(ShoppingCartDto shoppingCartDto);

    BookedProductsDto assembleProducts(AssemblyProductForOrderFromShoppingCartRequestDto
                                                                               assemblyProduct);

    AddProductToWarehouseRequestDto addProduct(AddProductToWarehouseRequestDto addProductToWarehouseRequestDto);

    AddressDto getAddress();
}
