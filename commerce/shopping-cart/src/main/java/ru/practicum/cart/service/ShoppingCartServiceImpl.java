package ru.practicum.cart.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.cart.exception.DataDuplicationException;
import ru.practicum.cart.exception.NoProductsInShoppingCartException;
import ru.practicum.cart.exception.NotFoundException;
import ru.practicum.cart.mapper.ShoppingCartMapper;
import ru.practicum.cart.model.ShoppingCart;
import ru.practicum.cart.repository.ShoppingCartRepository;
import ru.practicum.client.WarehouseClient;
import ru.practicum.dto.BookedProductsDto;
import ru.practicum.dto.ChangeProductQuantityRequestDto;
import ru.practicum.dto.ShoppingCartDto;

import java.util.List;
import java.util.Map;

@RequiredArgsConstructor
@Service
@Slf4j
public class ShoppingCartServiceImpl implements ShoppingCartService {
    private final ShoppingCartRepository shoppingCartRepository;
    private final ShoppingCartMapper shoppingCartMapper;
    private final WarehouseClient warehouseClient;

    @Override
    @Transactional(readOnly = true)
    public ShoppingCartDto findShoppingCart(String userName, String shoppingCartId) {
        log.info("Find shopping cart");
        ShoppingCart shoppingCart;

        if (userName == null && shoppingCartId == null) {
            throw new NotFoundException("Username and shopping cart id is null");
        }

        if (userName != null && shoppingCartId != null) {
            shoppingCart = shoppingCartRepository.findByIdAndUserName(shoppingCartId, userName)
                    .orElseThrow(() -> new NotFoundException("Shopping cart not found"));
        } else if (userName != null) {
            shoppingCart = shoppingCartRepository.findByUserName(userName)
                    .orElseThrow(() -> new NotFoundException("Shopping cart not found"));
        } else {
            shoppingCart = shoppingCartRepository.findById(shoppingCartId)
                    .orElseThrow(() -> new NotFoundException("Shopping cart not found"));
        }

        log.info("Found shopping cart by user name: {}", shoppingCart);

        return shoppingCartMapper.shoppingCartToShoppingCartDto(shoppingCart);
    }

    @Override
    @Transactional
    public ShoppingCartDto saveShoppingCart(String userName, Map<String, Integer> products) {
        log.info("Saving shopping cart {}", userName);

        if (shoppingCartRepository.existsByUserName(userName)) {
            throw new DataDuplicationException("This user already has an active shopping cart");
        }

        ShoppingCart shoppingCart = new ShoppingCart("gerger", userName, products);
        shoppingCart = shoppingCartRepository.save(shoppingCart);
        log.info("Saved shopping cart {}", userName);

        return shoppingCartMapper.shoppingCartToShoppingCartDto(shoppingCart);
    }

    @Override
    @Transactional
    public void deleteShoppingCart(String userName) {
        log.info("Deleting shopping cart {}", userName);
        shoppingCartRepository.deleteByUserName(userName);
        log.info("Deleted shopping cart {}", userName);
    }

    @Override
    @Transactional
    public ShoppingCartDto updateShoppingCart(String userName, List<String> products) {
        log.info("Updating shopping cart {}", userName);

        ShoppingCart shoppingCart = shoppingCartRepository.findByUserName(userName)
                .orElseThrow(() -> new NotFoundException("Shopping cart not found"));

        Map<String, Integer> quantityProducts = shoppingCart.getProducts();
        products.forEach(quantityProducts::remove);
        log.info("Updated shopping cart {}", userName);

        return shoppingCartMapper.shoppingCartToShoppingCartDto(shoppingCart);
    }

    @Override
    @Transactional
    public ShoppingCartDto changeProductQuantity(String userName,
                                                 ChangeProductQuantityRequestDto quantity) {
        log.info("Changing product quantity for user {}", userName);

        ShoppingCart shoppingCart = shoppingCartRepository.findByUserName(userName)
                .orElseThrow(() -> new NotFoundException("Shopping cart not found"));
        Map<String, Integer> products = shoppingCart.getProducts();

        if (!products.containsKey(quantity.getProductId())) {
            throw new NoProductsInShoppingCartException("There are no desired items in the shopping cart");
        }

        products.replace(quantity.getProductId(), quantity.getNewQuantity());

        return shoppingCartMapper.shoppingCartToShoppingCartDto(shoppingCart);
    }

    @Override
    @Transactional
    public BookedProductsDto bookingProducts(String userName) {
        log.info("Booking products for user {}", userName);

        ShoppingCart shoppingCart = shoppingCartRepository.findByUserName(userName)
                .orElseThrow(() -> new NotFoundException("Shopping cart not found"));

        return warehouseClient.bookingProducts(shoppingCartMapper.shoppingCartToShoppingCartDto(shoppingCart));
    }
}
