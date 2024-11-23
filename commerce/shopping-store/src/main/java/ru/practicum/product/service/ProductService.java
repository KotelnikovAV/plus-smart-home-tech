package ru.practicum.product.service;

import ru.practicum.product.dto.PageableDto;
import ru.practicum.product.dto.ProductCategory;
import ru.practicum.product.dto.ProductDto;
import ru.practicum.product.dto.QuantityState;

import java.util.List;

public interface ProductService {
    PageableDto findAllProducts(ProductCategory category, Integer page, Integer size, List<String> sort, String sortOrder);

    ProductDto saveProduct(ProductDto productDto);

    ProductDto updateProduct(ProductDto productDto);

    Boolean deleteProduct(Long productId);

    Boolean setQuantity(Long productId, QuantityState quantityState);

    ProductDto findProductById(Long productId);
}
