package com.dawanproject.booktracker.mappers;

import com.dawanproject.booktracker.dtos.BookDto;
import com.dawanproject.booktracker.entities.Book;
import org.mapstruct.*;
import org.mapstruct.factory.Mappers;

import java.time.format.DateTimeFormatter;

@Mapper(componentModel = "spring")
public interface BookMapper {

    BookMapper INSTANCE = Mappers.getMapper(BookMapper.class);

    DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ISO_LOCAL_DATE;

    // --- Book → BookDto ---
    @Mapping(target = "id", source = "book.bookId")
    @Mapping(target = "idVolume", source = "book.idVolume")
    @Mapping(target = "title", source = "book.title")
    @Mapping(target = "description", source = "book.description")
    @Mapping(target = "pageNumber", source = "book.pageNumber")
    @Mapping(target = "cover", source = "book.cover")
    @Mapping(target = "publicationDate", expression = "java(formatDate(book.getPublicationDate()))")
    @Mapping(target = "author", expression = "java(formatAuthor(book))")
    @Mapping(target = "category", expression = "java(formatCategory(book))")
    BookDto toDto(Book book);

    // --- BookDto → Book (pour save/update si besoin plus tard) ---
    @InheritInverseConfiguration
    Book toEntity(BookDto dto);

    // --- Méthodes utilitaires ---
    default String formatDate(java.time.LocalDate date) {
        return date != null ? DATE_FORMATTER.format(date) : null;
    }

    default String formatAuthor(Book book) {
        if (book == null || book.getAuthor() == null) return null;
        var a = book.getAuthor();
        String name = (nullSafe(a.getFirstname()) + " " + nullSafe(a.getLastname())).trim();
        return name.isBlank() ? null : name;
    }

    default String formatCategory(Book book) {
        if (book == null || book.getCategory() == null) return null;
        return book.getCategory().getGenre();
    }

    default String nullSafe(String value) {
        return value == null ? "" : value;
    }
}
