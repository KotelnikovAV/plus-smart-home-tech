package ru.practicum.client;

import jakarta.validation.Valid;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import ru.practicum.dto.*;
import ru.practicum.fallback.WarehouseFallback;

import java.util.Map;

@FeignClient(name = "warehouse", fallback = WarehouseFallback.class)
public interface WarehouseClient {
    @PutMapping("/api/v1/warehouse")
    NewProductInWarehouseRequestDto saveProduct(@RequestBody @Valid NewProductInWarehouseRequestDto newProduct);

    @PostMapping("/api/v1/warehouse/return")
    void returnProducts(@RequestBody Map<String, Integer> products);

    @PostMapping("/api/v1/warehouse/check")
    BookedProductsDto checkProducts(@RequestBody @Valid ShoppingCartDto shoppingCart);

    @PostMapping("/api/v1/warehouse/assembly")
    BookedProductsDto assembleProducts
            (@RequestBody @Valid AssemblyProductForOrderFromShoppingCartRequestDto assembly);

    @PostMapping("/api/v1/warehouse/add")
    AddProductToWarehouseRequestDto addProduct(@RequestBody @Valid AddProductToWarehouseRequestDto addProduct);

    @GetMapping("/api/v1/warehouse/address")
    AddressDto getAddress();
}
