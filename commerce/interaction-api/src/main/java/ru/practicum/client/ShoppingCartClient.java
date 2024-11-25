package ru.practicum.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;
import ru.practicum.dto.BookedProductsDto;
import ru.practicum.dto.ChangeProductQuantityRequestDto;
import ru.practicum.dto.ShoppingCartDto;

import java.util.List;
import java.util.Map;

@FeignClient(name = "shopping-cart")
public interface ShoppingCartClient {
    @GetMapping("/api/v1/shopping-cart")
    ShoppingCartDto findShoppingCart(@RequestParam(required = false) String username,
                                     @RequestParam(required = false) String shoppingCartId);

    @PutMapping("/api/v1/shopping-cart")
    ShoppingCartDto saveShoppingCart(@RequestParam String username,
                                     @RequestBody Map<String, Integer> products);

    @DeleteMapping("/api/v1/shopping-cart")
    void deleteShoppingCart(@RequestParam String username);

    @PostMapping("/api/v1/shopping-cart/remove")
    ShoppingCartDto updateShoppingCart(@RequestParam String username,
                                       @RequestBody List<String> products);

    @PostMapping("/api/v1/shopping-cart/change-quantity")
    ShoppingCartDto changeProductQuantity(@RequestParam String username,
                                          @RequestBody ChangeProductQuantityRequestDto quantity);

    @PostMapping("/api/v1/shopping-cart/booking")
    BookedProductsDto bookingProducts(@RequestParam String username);
}
