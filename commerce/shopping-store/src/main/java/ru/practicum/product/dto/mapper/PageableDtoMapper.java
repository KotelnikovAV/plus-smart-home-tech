package ru.practicum.product.dto.mapper;

import lombok.experimental.UtilityClass;
import ru.practicum.product.dto.PageableDto;
import ru.practicum.product.dto.ProductDto;
import ru.practicum.product.dto.SortDto;

import java.util.ArrayList;
import java.util.List;

@UtilityClass
public class PageableDtoMapper {
    public PageableDto toPageableDto(List<ProductDto> products, List<String> sort, String sortOrder) {
        return PageableDto.builder()
                .content(products)
                .sort(getSortDto(sort, sortOrder))
                .build();
    }

    private List<SortDto> getSortDto(List<String> sort, String sortOrder) {
        List<SortDto> sortsDto = new ArrayList<>();

        for (String s : sort) {
            sortsDto.add(new SortDto(sortOrder, s));
        }

        return sortsDto;
    }
}
