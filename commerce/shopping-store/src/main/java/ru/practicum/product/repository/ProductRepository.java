package ru.practicum.product.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import ru.practicum.dto.ProductCategory;
import ru.practicum.product.model.Product;

public interface ProductRepository extends JpaRepository<Product, Long> {
    Page<Product> findByProductCategory(ProductCategory productCategory, Pageable pageable);
}
