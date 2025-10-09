package com.dawanproject.booktracker.mappers;

import com.dawanproject.booktracker.dtos.CategoryDto;
import com.dawanproject.booktracker.entities.Category;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface CategoryMapper {
    @Mapping(source = "categoryId", target = "id")
    CategoryDto toDto(Category category);

    @Mapping(source = "id", target = "categoryId")
    Category toEntity(CategoryDto dto);
}
