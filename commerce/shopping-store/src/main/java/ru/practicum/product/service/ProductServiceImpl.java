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

import java.util.*;

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
                sortOrder);
    }

    @Transactional
    @Override
    public ProductDto saveProduct(ProductDto productDto) {
        log.info("Saving product");

        Product product = productMapper.productDtoToProduct(productDto);

        if (productDto.getProductId() == null || productDto.getProductId().isEmpty()) {
            product.setProductId(UUID.randomUUID().toString());
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

        if (productId == null || productId.isEmpty()) {
            throw new NotFoundException("productId is empty");
        }
        productId = productId.substring(1, productId.length() - 1);

        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new NotFoundException("Product not found"));
        product.setProductState(ProductState.DEACTIVATE);
        log.info("Product {} deleted", productId);

        return true;
    }

    @Transactional
    @Override
    public Boolean setQuantity(String productId, QuantityState quantityState) {
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

    @Transactional(readOnly = true)
    @Override
    public Double findPrice(Map<String, Integer> products) {
        log.info("Finding price");

        Set<String> productIds = products.keySet();
        List<Product> productList = productRepository.findAllById(productIds);
        Double price = productList.stream().
                map(Product::getPrice).reduce(0.0, Double::sum);
        log.info("Found price {} products", price);
        return price;
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