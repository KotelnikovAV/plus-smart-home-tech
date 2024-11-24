package ru.practicum.warehouse.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.client.WarehouseClient;
import ru.practicum.dto.*;
import ru.practicum.warehouse.service.WarehouseService;

import java.util.Map;

@RestController
@RequestMapping("api/v1/warehouse")
@RequiredArgsConstructor
@Slf4j
@Validated
public class WarehouseController implements WarehouseClient {
    private final WarehouseService warehouseService;

    @PutMapping
    public NewProductInWarehouseRequestDto saveProduct(@RequestBody @Valid NewProductInWarehouseRequestDto newProduct) {
        log.info("Saving new product in warehouse: {}", newProduct);
        return warehouseService.saveProduct(newProduct);
    }

    @PostMapping("return")
    public void returnProducts(@RequestBody Map<String, Integer> products) {
        log.info("Returning products: {}", products);
        warehouseService.returnProducts(products);
    }

    @Override
    @PostMapping("booking")
    public BookedProductsDto bookingProducts(@RequestBody @Valid ShoppingCartDto shoppingCart) {
        log.info("Booking products: {}", shoppingCart);
        return warehouseService.bookingProducts(shoppingCart);
    }

    @PostMapping("assembly")
    public AssemblyProductForOrderFromShoppingCartRequestDto assembleProducts
            (@RequestBody @Valid AssemblyProductForOrderFromShoppingCartRequestDto assembly) {
        log.info("Assembly products: {}", assembly);
        return warehouseService.assembleProducts(assembly);
    }

    @PostMapping("add")
    public AddProductToWarehouseRequestDto addProduct(@RequestBody @Valid AddProductToWarehouseRequestDto addProduct) {
        log.info("Add product: {}", addProduct);
        return warehouseService.addProduct(addProduct);
    }

    @GetMapping("address")
    public AddressDto getAddress() {
        log.info("Get address");
        return warehouseService.getAddress();
    }
}
