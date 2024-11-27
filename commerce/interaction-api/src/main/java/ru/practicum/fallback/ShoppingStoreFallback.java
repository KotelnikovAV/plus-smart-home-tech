package ru.practicum.fallback;

import org.springframework.stereotype.Component;
import ru.practicum.client.ShoppingStoreClient;
import ru.practicum.dto.PageableDto;
import ru.practicum.dto.ProductCategory;
import ru.practicum.dto.ProductDto;
import ru.practicum.dto.QuantityState;
import ru.practicum.exception.ServerUnavailableException;

import java.util.List;

@Component
public class ShoppingStoreFallback implements ShoppingStoreClient {

    @Override
    public PageableDto findAllProducts(ProductCategory category, Integer page, Integer size, List<String> sort, String sortOrder) {
        throw new ServerUnavailableException("Endpoint /api/v1/shopping-store method GET is unavailable");
    }

    @Override
    public ProductDto updateProduct(ProductDto productDto) {
        throw new ServerUnavailableException("Endpoint /api/v1/shopping-store method POST is unavailable");
    }

    @Override
    public ProductDto saveProduct(ProductDto productDto) {
        throw new ServerUnavailableException("Endpoint /api/v1/shopping-store method PUT is unavailable");
    }

    @Override
    public Boolean deleteProduct(String productId) {
        throw new ServerUnavailableException("Endpoint /api/v1/shopping-store/removeProductFromStore method POST is unavailable");
    }

    @Override
    public Boolean setQuantity(String productId, QuantityState quantityState) {
        throw new ServerUnavailableException("Endpoint /api/v1/shopping-store/quantityState method POST is unavailable");
    }

    @Override
    public ProductDto findProductById(String productId) {
        throw new ServerUnavailableException("Endpoint /api/v1/shopping-store/" + productId + " method POST is unavailable");
    }
}
