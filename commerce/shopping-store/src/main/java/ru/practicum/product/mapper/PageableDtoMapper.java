package ru.practicum.product.mapper;

import lombok.experimental.UtilityClass;
import ru.practicum.dto.PageableDto;
import ru.practicum.dto.ProductDto;
import ru.practicum.dto.SortDto;

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
