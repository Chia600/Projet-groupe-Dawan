package com.dawanproject.booktracker.mappers;

import com.dawanproject.booktracker.dtos.AuthorDto;
import com.dawanproject.booktracker.entities.Author;
import org.mapstruct.*;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING, unmappedTargetPolicy = ReportingPolicy.IGNORE)
@DecoratedWith(AuthorMapperDecorator.class)
public interface AuthorMapper {

    @Mapping(source = "books", target = "bookIds")
    AuthorDto toDto(Author author);

    Author toEntity(AuthorDto dto);

}
