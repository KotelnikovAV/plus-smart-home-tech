package ru.practicum.product.service;

import ru.practicum.dto.PageableDto;
import ru.practicum.dto.ProductCategory;
import ru.practicum.dto.ProductDto;
import ru.practicum.dto.QuantityState;

import java.util.List;
import java.util.Map;

public interface ProductService {
    PageableDto findAllProducts(ProductCategory category, Integer page, Integer size, List<String> sort, String sortOrder);

    ProductDto saveProduct(ProductDto productDto);

    ProductDto updateProduct(ProductDto productDto);

    Boolean deleteProduct(String productId);

    Boolean setQuantity(String productId, QuantityState quantityState);

    ProductDto findProductById(String productId);

    Double findPrice(Map<String, Integer> products);
}
