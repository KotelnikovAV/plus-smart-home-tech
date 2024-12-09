package ru.practicum.cart.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.cart.service.ShoppingCartService;
import ru.practicum.client.ShoppingCartClient;
import ru.practicum.dto.BookedProductsDto;
import ru.practicum.dto.ChangeProductQuantityRequestDto;
import ru.practicum.dto.ShoppingCartDto;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("api/v1/shopping-cart")
@RequiredArgsConstructor
@Slf4j
@Validated
public class ShoppingCartController implements ShoppingCartClient {
    private final ShoppingCartService shoppingCartService;

    @Override
    @GetMapping
    public ShoppingCartDto findShoppingCart(@RequestParam(required = false) String username,
                                            @RequestParam(required = false) String shoppingCartId) {
        log.info("Find shopping cart by name: {}", username);
        return shoppingCartService.findShoppingCart(username, shoppingCartId);
    }

    @Override
    @PutMapping
    public ShoppingCartDto saveShoppingCart(@RequestParam String username,
                                            @RequestBody Map<String, Integer> products) {
        log.info("Save shopping cart by name: {}", username);
        return shoppingCartService.saveShoppingCart(username, products);
    }

    @Override
    @DeleteMapping
    public void deleteShoppingCart(@RequestParam String username) {
        log.info("Delete shopping cart by name: {}", username);
        shoppingCartService.deleteShoppingCart(username);
    }

    @Override
    @PostMapping("remove")
    public ShoppingCartDto updateShoppingCart(@RequestParam String username,
                                              @RequestBody List<String> products) {
        log.info("Update shopping cart by name: {}", username);
        return shoppingCartService.updateShoppingCart(username, products);
    }

    @Override
    @PostMapping("change-quantity")
    public ShoppingCartDto changeProductQuantity(@RequestParam String username,
                                                 @RequestBody ChangeProductQuantityRequestDto quantity) {
        log.info("Change product quantity: {}", quantity);
        return shoppingCartService.changeProductQuantity(username, quantity);
    }

    @Override
    @PostMapping("booking")
    public BookedProductsDto bookingProducts(@RequestParam String username) {
        log.info("Booking products: {}", username);
        return shoppingCartService.bookingProducts(username);
    }

    @GetMapping("{username}")
    @Override
    public List<ShoppingCartDto> findShoppingCartsByUsername(@PathVariable String username) {
        log.info("Find shopping cart by name: {}", username);
        return shoppingCartService.findShoppingCartsIdByUsername(username);
    }
}
