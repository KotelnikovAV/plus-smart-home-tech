package ru.practicum.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.dto.BookedProductsDto;
import ru.practicum.dto.ChangeProductQuantityRequestDto;
import ru.practicum.dto.ShoppingCartDto;
import ru.practicum.service.ShoppingCartService;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("api/v1/shopping-cart")
@RequiredArgsConstructor
@Slf4j
@Validated
public class ShoppingCartController {
    private final ShoppingCartService shoppingCartService;

    @GetMapping
    public ShoppingCartDto findShoppingCart(@RequestParam String username) {
        log.info("Find shopping cart by name: {}", username);
        return shoppingCartService.findShoppingCart(username);
    }

    @PutMapping
    public ShoppingCartDto saveShoppingCart(@RequestParam String username,
                                            @RequestBody Map<String, Integer> products) {
        log.info("Save shopping cart by name: {}", username);
        return shoppingCartService.saveShoppingCart(username, products);
    }

    @DeleteMapping
    public void deleteShoppingCart(@RequestParam String username) {
        log.info("Delete shopping cart by name: {}", username);
        shoppingCartService.deleteShoppingCart(username);
    }

    @PostMapping("remove")
    public ShoppingCartDto updateShoppingCart(@RequestParam String username,
                                              @RequestBody List<String> products) {
        log.info("Update shopping cart by name: {}", username);
        return shoppingCartService.updateShoppingCart(username, products);
    }

    @PostMapping("change-quantity")
    public ShoppingCartDto changeProductQuantity(@RequestParam String username,
                                                 @RequestBody ChangeProductQuantityRequestDto quantity) {
        log.info("Change product quantity: {}", quantity);
        return shoppingCartService.changeProductQuantity(username, quantity);
    }

    @PostMapping("booking")
    public BookedProductsDto bookingProducts(@RequestParam String username) {
        log.info("Booking products: {}", username);
        return shoppingCartService.bookingProducts(username);
    }
}
