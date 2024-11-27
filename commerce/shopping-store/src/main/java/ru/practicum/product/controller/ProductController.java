package ru.practicum.product.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.client.ShoppingStoreClient;
import ru.practicum.dto.PageableDto;
import ru.practicum.dto.ProductCategory;
import ru.practicum.dto.ProductDto;
import ru.practicum.dto.QuantityState;
import ru.practicum.product.service.ProductService;

import java.util.List;

@RestController
@RequestMapping("api/v1/shopping-store")
@RequiredArgsConstructor
@Slf4j
@Validated
public class ProductController implements ShoppingStoreClient {
    private final ProductService productService;

    @Override
    @GetMapping
    public PageableDto findAllProducts(@RequestParam ProductCategory category,
                                       @RequestParam(defaultValue = "0") Integer page,
                                       @RequestParam(defaultValue = "1") Integer size,
                                       @RequestParam(required = false) List<String> sort,
                                       @RequestParam(defaultValue = "ASC") String sortOrder) {
        log.info("findAllProducts called. Category = {}, page = {}, size = {}, sort = {}", category, page, size, sort);
        return productService.findAllProducts(category, page, size, sort, sortOrder);
    }

    @Override
    @PostMapping
    public ProductDto updateProduct(@RequestBody @Valid ProductDto productDto) {
        log.info("updateProduct called. productDto = {}", productDto);
        return productService.updateProduct(productDto);
    }

    @Override
    @PutMapping
    public ProductDto saveProduct(@RequestBody @Valid ProductDto productDto) {
        log.info("saveProduct called. productDto = {}", productDto);
        return productService.saveProduct(productDto);
    }

    @Override
    @PostMapping("removeProductFromStore")
    public Boolean deleteProduct(@RequestBody String productId) {
        log.info("deleteProduct called. productId = {}", productId);
        return productService.deleteProduct(productId);
    }

    @Override
    @PostMapping("quantityState")
    public Boolean setQuantity(@RequestParam String productId,
                               @RequestParam QuantityState quantityState) {
        log.info("setQuantity called. productId = {}, quantityState = {}", productId, quantityState);
        return productService.setQuantity(productId, quantityState);
    }

    @Override
    @GetMapping("{productId}")
    public ProductDto findProductById(@PathVariable String productId) {
        log.info("findProductById called. productId = {}", productId);
        return productService.findProductById(productId);
    }
}
