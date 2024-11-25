package ru.practicum.client;

import jakarta.validation.Valid;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;
import ru.practicum.dto.PageableDto;
import ru.practicum.dto.ProductCategory;
import ru.practicum.dto.ProductDto;
import ru.practicum.dto.QuantityState;
import ru.practicum.fallback.ShoppingStoreFallback;

import java.util.List;

@FeignClient(name = "shopping-store", fallback = ShoppingStoreFallback.class)
public interface ShoppingStoreClient {
    @GetMapping("/api/v1/shopping-store")
    PageableDto findAllProducts(@RequestParam ProductCategory category,
                                @RequestParam(defaultValue = "0") Integer page,
                                @RequestParam(defaultValue = "1") Integer size,
                                @RequestParam(required = false) List<String> sort,
                                @RequestParam(defaultValue = "ASC") String sortOrder);

    @PostMapping("/api/v1/shopping-store")
    ProductDto updateProduct(@RequestBody @Valid ProductDto productDto);

    @PutMapping("/api/v1/shopping-store")
    ProductDto saveProduct(@RequestBody @Valid ProductDto productDto);

    @PostMapping("/api/v1/shopping-store/removeProductFromStore")
    Boolean deleteProduct(@RequestBody String productId);

    @PostMapping("/api/v1/shopping-store/quantityState")
    Boolean setQuantity(@RequestParam String productId,
                        @RequestParam QuantityState quantityState);

    @GetMapping("/api/v1/shopping-store/{productId}")
    ProductDto findProductById(@PathVariable String productId);
}
