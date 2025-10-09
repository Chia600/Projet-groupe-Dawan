package com.dawanproject.booktracker.mappers;
import com.dawanproject.booktracker.dtos.BookDto;
import com.dawanproject.booktracker.entities.Book;
import org.mapstruct.*;
@Mapper(componentModel = MappingConstants.ComponentModel.SPRING, unmappedTargetPolicy = ReportingPolicy.IGNORE)
@DecoratedWith(BookMapperDecorator.class)
public interface BookMapper {
    @Mapping(source = "bookId", target = "id")
    @Mapping(source = "category.genre", target = "category")
    @Mapping(target = "author", ignore = true)
    BookDto toDto(Book book);

    @Mapping(source = "id", target = "bookId")
    @Mapping(target = "category.genre", source = "category")
    @Mapping(target = "author", ignore = true)
    Book toEntity(BookDto dto);
}
