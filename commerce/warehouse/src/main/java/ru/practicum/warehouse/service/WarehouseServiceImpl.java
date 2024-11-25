package ru.practicum.warehouse.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.client.ShoppingCartClient;
import ru.practicum.dto.*;
import ru.practicum.warehouse.exception.NoSpecifiedProductInWarehouseException;
import ru.practicum.warehouse.exception.NotFoundException;
import ru.practicum.warehouse.mapper.WarehouseMapper;
import ru.practicum.warehouse.model.Dimension;
import ru.practicum.warehouse.model.Warehouse;
import ru.practicum.warehouse.repository.WarehouseRepository;

import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
@Slf4j
public class WarehouseServiceImpl implements WarehouseService {
    private final WarehouseRepository warehouseRepository;
    private final WarehouseMapper warehouseMapper;
    private final ShoppingCartClient shoppingCartClient;

    @Transactional
    @Override
    public NewProductInWarehouseRequestDto saveProduct(NewProductInWarehouseRequestDto newProductInWarehouseRequestDto) {
        log.info("Saving new product in warehouse");

        Warehouse warehouse = warehouseMapper.warehouseDtoToWarehouse(newProductInWarehouseRequestDto);
        warehouse.setQuantity(0);
        warehouse = warehouseRepository.save(warehouse);
        log.info("New product in warehouse saved");

        return warehouseMapper.warehouseToWarehouseDto(warehouse);
    }

    @Transactional
    @Override
    public void returnProducts(Map<String, Integer> returnProducts) {
        log.info("Returning products");

        Set<String> productIds = returnProducts.keySet();
        List<Warehouse> warehouses = warehouseRepository.findAllById(productIds);

        if (warehouses.size() != productIds.size()) {
            throw new NoSpecifiedProductInWarehouseException("All items were not found during the return");
        }

        Map<String, Warehouse> warehouseMap = warehouses.stream()
                .collect(Collectors.toMap(Warehouse::getProductId, warehouse -> warehouse));

        for (String productId : productIds) {
            Warehouse warehouse = warehouseMap.get(productId);
            warehouse.setQuantity(warehouse.getQuantity() + returnProducts.get(productId));
        }

        log.info("Returned products");
    }

    @Transactional
    @Override
    public BookedProductsDto bookingProducts(ShoppingCartDto shoppingCartDto) {
        log.info("Booking products");
        Map<String, Integer> quantityProducts = shoppingCartDto.getProducts();

        Set<String> productIds = quantityProducts.keySet();
        List<Warehouse> warehouses = warehouseRepository.findAllById(productIds);
        Map<String, Warehouse> warehouseMap = warehouses.stream()
                .collect(Collectors.toMap(Warehouse::getProductId, warehouse -> warehouse));

        for (String productId : productIds) {
            Warehouse warehouse = warehouseMap.get(productId);
            warehouse.setQuantity(warehouse.getQuantity() - quantityProducts.get(productId));
        }

        log.info("Booked products");

        return getBookedProducts(quantityProducts, warehouses);
    }

    @Transactional(readOnly = true)
    @Override
    public BookedProductsDto assembleProducts
            (AssemblyProductForOrderFromShoppingCartRequestDto assemblyProduct) {
        log.info("Assembly products");

        ShoppingCartDto shoppingCartDto = shoppingCartClient
                .findShoppingCart(null, assemblyProduct.getShoppingCartId());

        Map<String, Integer> quantityProducts = shoppingCartDto.getProducts();
        Set<String> productIds = quantityProducts.keySet();
        List<Warehouse> warehouses = warehouseRepository.findAllById(productIds);

        return getBookedProducts(quantityProducts, warehouses);
    }

    @Transactional
    @Override
    public AddProductToWarehouseRequestDto addProduct(AddProductToWarehouseRequestDto addProductToWarehouseRequestDto) {
        log.info("Adding product to warehouse");

        Warehouse warehouse = warehouseRepository.findById(addProductToWarehouseRequestDto.getProductId())
                .orElseThrow(() -> new NotFoundException("Product not found"));
        warehouse.setQuantity(warehouse.getQuantity() + addProductToWarehouseRequestDto.getQuantity());
        log.info("Product added to warehouse");

        return addProductToWarehouseRequestDto;
    }

    @Override
    public AddressDto getAddress() {
        log.info("Getting address");

        return AddressDto.builder()
                .country("country")
                .city("city")
                .street("street")
                .house("house")
                .flat("flat")
                .build();
    }

    private BookedProductsDto getBookedProducts(Map<String, Integer> quantityProducts, List<Warehouse> warehouse) {
        double weight = 0.0;
        double volume = 0.0;

        BookedProductsDto bookedProductsDto = new BookedProductsDto();

        for (Warehouse w : warehouse) {
            Dimension dimension = w.getDimension();
            weight = weight + w.getWeight() * quantityProducts.get(w.getProductId());
            volume = volume + dimension.getDepth() * dimension.getHeight() * dimension.getWidth()
                    * quantityProducts.get(w.getProductId());
        }

        bookedProductsDto.setDeliveryWeight(weight);
        bookedProductsDto.setDeliveryVolume(volume);
        bookedProductsDto.setFragile(warehouse.stream()
                .map(Warehouse::isFragile)
                .reduce(false, (a, b) -> a || b));

        return bookedProductsDto;
    }
}
