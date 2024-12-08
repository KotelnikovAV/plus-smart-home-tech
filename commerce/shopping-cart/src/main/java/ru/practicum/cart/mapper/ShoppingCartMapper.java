package ru.practicum.cart.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;
import ru.practicum.cart.model.ShoppingCart;
import ru.practicum.dto.ShoppingCartDto;

import java.util.List;


@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface ShoppingCartMapper {
    @Mapping(target = "shoppingCartId", source = "id")
    ShoppingCartDto shoppingCartToShoppingCartDto(ShoppingCart shoppingCart);

    List<ShoppingCartDto> shoppingCartListToShoppingCartDtoList(List<ShoppingCart> shoppingCart);
}
