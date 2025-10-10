package com.dawanproject.booktracker.mappers;

import com.dawanproject.booktracker.dtos.AuthorDto;
import com.dawanproject.booktracker.entities.Author;
import com.dawanproject.booktracker.entities.Book;
import org.mapstruct.*;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING, unmappedTargetPolicy = ReportingPolicy.IGNORE)
@DecoratedWith(AuthorMapperDecorator.class)
public interface AuthorMapper {

    @Mapping(source = "authorId", target = "id")
    @Mapping(source = "books", target = "bookIds", qualifiedByName = "mapBooksToIds")
    AuthorDto toDto(Author author);

    @Mapping(source = "id", target = "authorId")
    @Mapping(target = "books", ignore = true)
    Author toEntity(AuthorDto dto);

    @Named("mapBooksToIds")
    default List<Long> mapBooksToIds(Set<Book> books) {
        if (books.isEmpty() || books == null) {
            return null;
        }
        return books.stream()
                .map(b -> b.getBookId())
                .collect(Collectors.toList());
    }

}
