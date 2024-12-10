package ru.practicum.delivery.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;
import ru.practicum.delivery.model.Address;
import ru.practicum.delivery.model.Delivery;
import ru.practicum.dto.AddressDto;
import ru.practicum.dto.DeliveryDto;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface DeliveryMapper {
    Address addressDtoToAddress(AddressDto addressDto);

    AddressDto addressToAddressDto(Address address);

    DeliveryDto deliveryToDeliveryDto(Delivery delivery);

    Delivery deliveryDtoToDelivery(DeliveryDto deliveryDto);
}
