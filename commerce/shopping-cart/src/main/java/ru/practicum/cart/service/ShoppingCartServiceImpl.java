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
import java.util.UUID;

@RequiredArgsConstructor
@Service
@Slf4j
public class ShoppingCartServiceImpl implements ShoppingCartService {
    private final ShoppingCartRepository shoppingCartRepository;
    private final ShoppingCartMapper shoppingCartMapper;
    private final WarehouseClient warehouseClient;

    @Override
    @Transactional(readOnly = true)
    public ShoppingCartDto findShoppingCart(String username, String shoppingCartId) {
        log.info("Find shopping cart");
        ShoppingCart shoppingCart;

        if (username == null && shoppingCartId == null) {
            throw new NotFoundException("Username and shopping cart id is null");
        }

        if (username != null && shoppingCartId != null) {
            shoppingCart = shoppingCartRepository.findByIdAndUsername(shoppingCartId, username)
                    .orElseThrow(() -> new NotFoundException("Shopping cart not found"));
        } else if (username != null) {
            shoppingCart = shoppingCartRepository.findByUsernameAndActive(username, true)
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
    public ShoppingCartDto saveShoppingCart(String username, Map<String, Integer> products) {
        log.info("Saving shopping cart {}", username);

        if (shoppingCartRepository.existsByUsername(username)) {
            throw new DataDuplicationException("This user already has an active shopping cart");
        }

        ShoppingCart shoppingCart = new ShoppingCart(UUID.randomUUID().toString(), username, true, products);
        shoppingCart = shoppingCartRepository.save(shoppingCart);
        warehouseClient.checkProducts(shoppingCartMapper.shoppingCartToShoppingCartDto(shoppingCart));
        log.info("Saved shopping cart {}", username);

        return shoppingCartMapper.shoppingCartToShoppingCartDto(shoppingCart);
    }

    @Override
    @Transactional
    public void deleteShoppingCart(String username) {
        log.info("Deleting shopping cart {}", username);
        shoppingCartRepository.deleteByUsernameAndActive(username, true);
        log.info("Deleted shopping cart {}", username);
    }

    @Override
    @Transactional
    public ShoppingCartDto updateShoppingCart(String username, List<String> products) {
        log.info("Updating shopping cart {}", username);

        ShoppingCart shoppingCart = shoppingCartRepository.findByUsernameAndActive(username, true)
                .orElseThrow(() -> new NotFoundException("Shopping cart not found"));

        Map<String, Integer> quantityProducts = shoppingCart.getProducts();
        products.forEach(quantityProducts::remove);
        log.info("Updated shopping cart {}", username);

        return shoppingCartMapper.shoppingCartToShoppingCartDto(shoppingCart);
    }

    @Override
    @Transactional
    public ShoppingCartDto changeProductQuantity(String username,
                                                 ChangeProductQuantityRequestDto quantity) {
        log.info("Changing product quantity for user {}", username);

        ShoppingCart shoppingCart = shoppingCartRepository.findByUsernameAndActive(username, true)
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
    public BookedProductsDto bookingProducts(String username) {
        log.info("Booking products for user {}", username);

        ShoppingCart shoppingCart = shoppingCartRepository.findByUsernameAndActive(username, true)
                .orElseThrow(() -> new NotFoundException("Shopping cart not found"));

        return warehouseClient.checkProducts(shoppingCartMapper.shoppingCartToShoppingCartDto(shoppingCart));
    }

    @Override
    @Transactional(readOnly = true)
    public List<ShoppingCartDto> findShoppingCartsIdByUsername(String username) {
        log.info("Finding shopping cart by id {}", username);
        List<ShoppingCart> shoppingCart = shoppingCartRepository.findByUsername(username);
        log.info("Found shopping carts {}", username);
        return shoppingCartMapper.shoppingCartListToShoppingCartDtoList(shoppingCart);
    }
}
