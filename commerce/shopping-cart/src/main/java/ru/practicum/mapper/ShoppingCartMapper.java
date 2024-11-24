package ru.practicum.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;
import ru.practicum.dto.ShoppingCartDto;
import ru.practicum.model.ShoppingCart;


@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface ShoppingCartMapper {
    @Mapping(target = "shoppingCartId", source = "id")
    ShoppingCartDto shoppingCartToShoppingCartDto(ShoppingCart shoppingCart);
}
