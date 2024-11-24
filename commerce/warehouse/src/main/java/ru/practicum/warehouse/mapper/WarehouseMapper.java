package ru.practicum.warehouse.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;
import ru.practicum.dto.DimensionDto;
import ru.practicum.dto.NewProductInWarehouseRequestDto;
import ru.practicum.warehouse.model.Dimension;
import ru.practicum.warehouse.model.Warehouse;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface WarehouseMapper {
    DimensionDto dimensionToDimensionDto(Dimension dimension);

    Dimension dimensionDtoToDimension(DimensionDto dimensionDto);

    Warehouse warehouseDtoToWarehouse(NewProductInWarehouseRequestDto warehouseRequestDto);

    NewProductInWarehouseRequestDto warehouseToWarehouseDto(Warehouse warehouse);
}
