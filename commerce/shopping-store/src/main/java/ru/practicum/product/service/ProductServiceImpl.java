package ru.practicum.product.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import ru.practicum.dto.*;
import ru.practicum.product.exception.NotFoundException;
import ru.practicum.product.mapper.PageableDtoMapper;
import ru.practicum.product.mapper.ProductMapper;
import ru.practicum.product.model.Product;
import ru.practicum.product.repository.ProductRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@RequiredArgsConstructor
@Service
@Slf4j
public class ProductServiceImpl implements ProductService {
    private final ProductRepository productRepository;
    private final ProductMapper productMapper;

    @Transactional(readOnly = true)
    @Override
    public PageableDto findAllProducts(ProductCategory category,
                                       Integer page,
                                       Integer size,
                                       List<String> sort,
                                       String sortOrder) {
        log.info("Finding products");

        Page<Product> products = productRepository.findByProductCategory(category,
                getPageRequest(page, size, sort, sortOrder));

        if (products.isEmpty()) {
            throw new NotFoundException("No products found");
        }

        log.info("Found {} products", products.getTotalElements());

        return PageableDtoMapper.toPageableDto(productMapper.productListToProductDtoList(products.getContent()),
                sort,
                sortOrder); // какой-то непонятный, невнятный вид ответа требуют тесты на выходе
    }

    @Transactional
    @Override
    public ProductDto saveProduct(ProductDto productDto) {
        log.info("Saving product");

        Product product = productMapper.productDtoToProduct(productDto);

        if (productDto.getProductId() == null || productDto.getProductId().isEmpty()) {
            product.setProductId(UUID.randomUUID().toString()); // спецификация API обозначает, что id это значение
            // типа String и должно выглядеть следующим образом - "3fa85f64-5717-4562-b3fc-2c963f66afa6", но при
            // этом на вход id не дается, приходится задавать самому
        }

        product = productRepository.save(product);
        log.info("Saved product");

        return productMapper.productToProductDto(product);
    }

    @Transactional
    @Override
    public ProductDto updateProduct(ProductDto productDto) {
        log.info("Updating product {}", productDto);

        Product product = productRepository.findById(productDto.getProductId())
                .orElseThrow(() -> new NotFoundException("Product not found"));

        updateFieldsProduct(product, productDto);
        log.info("Product {} updated", productDto.getProductId());

        return productMapper.productToProductDto(product);
    }

    @Transactional
    @Override
    public Boolean deleteProduct(String productId) {
        log.info("Deleting product {}", productId);
        productId = productId.substring(1, productId.length() - 1); // какие-то странные тесты,
        // здесь почему-то этот параметр передается в теле запроса, из-за этого строчка записывается
        // как ""......"" - с двойными кавычками, поэтому приходится их убирать

        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new NotFoundException("Product not found"));
        product.setProductState(ProductState.DEACTIVATE);
        log.info("Product {} deleted", productId);

        return true;
    }

    @Transactional
    @Override
    public Boolean setQuantity(String productId, QuantityState quantityState) { // здесь, например, productId
        // передается в качестве параметра запроса и поэтому проблем с кавычками не возникает
        log.info("Updating quantityState product {}", productId);

        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new NotFoundException("Product not found"));
        product.setQuantityState(quantityState);

        return true;
    }

    @Transactional(readOnly = true)
    @Override
    public ProductDto findProductById(String productId) {
        log.info("Finding product by id {}", productId);

        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new NotFoundException("Product not found"));

        log.info("Found product {}", product);

        return productMapper.productToProductDto(product);
    }

    private void updateFieldsProduct(Product product, ProductDto productDto) {
        product.setProductName(productDto.getProductName());
        product.setDescription(productDto.getDescription());
        product.setPrice(productDto.getPrice());
        product.setProductCategory(productDto.getProductCategory());
        product.setQuantityState(productDto.getQuantityState());
        product.setProductState(productDto.getProductState());

        if (productDto.getImageSrc() != null) {
            product.setImageSrc(productDto.getImageSrc());
        }

        if (productDto.getRating() != null) {
            product.setRating(productDto.getRating());
        }
    }

    private PageRequest getPageRequest(Integer page, Integer size, List<String> sort, String sortOrder) {
        PageRequest pageRequest;

        if (CollectionUtils.isEmpty(sort)) {
            pageRequest = PageRequest.of(page, size, Sort.by(Sort.Direction.valueOf(sortOrder),
                    "productName"));
        } else {
            List<Sort.Order> orders = new ArrayList<>();

            for (String s : sort) {
                orders.add(new Sort.Order(Sort.Direction.valueOf(sortOrder), s));
            }

            Sort sorts = Sort.by(orders);
            pageRequest = PageRequest.of(page, size, sorts);
        }
        return pageRequest;
    }
}