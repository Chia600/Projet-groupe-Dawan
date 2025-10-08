package com.dawanproject.booktracker.mappers;

import com.dawanproject.booktracker.dtos.AuthorDto;
import com.dawanproject.booktracker.entities.Author;
import org.mapstruct.*;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING, unmappedTargetPolicy = ReportingPolicy.IGNORE)
@DecoratedWith(AuthorMapperDecorator.class)
public interface AuthorMapper {

    @Mapping(source = "authorId", target = "id")
    @Mapping(target = "bookIds", ignore = true)
    AuthorDto toDto(Author author);

    @Mapping(source = "id", target = "authorId")
    @Mapping(target = "books", ignore = true)
    Author toEntity(AuthorDto dto);

}
