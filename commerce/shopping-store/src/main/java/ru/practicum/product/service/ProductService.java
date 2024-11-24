package ru.practicum.product.service;

import ru.practicum.dto.PageableDto;
import ru.practicum.dto.ProductCategory;
import ru.practicum.dto.ProductDto;
import ru.practicum.dto.QuantityState;

import java.util.List;

public interface ProductService {
    PageableDto findAllProducts(ProductCategory category, Integer page, Integer size, List<String> sort, String sortOrder);

    ProductDto saveProduct(ProductDto productDto);

    ProductDto updateProduct(ProductDto productDto);

    Boolean deleteProduct(Long productId);

    Boolean setQuantity(Long productId, QuantityState quantityState);

    ProductDto findProductById(Long productId);
}
