package com.dawanproject.booktracker.mappers;

import com.dawanproject.booktracker.dtos.AuthorDto;
import com.dawanproject.booktracker.entities.Author;
import com.dawanproject.booktracker.entities.Book;
import com.dawanproject.booktracker.repositories.BookRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

import java.util.Set;
import java.util.stream.Collectors;

public abstract class AuthorMapperDecorator implements AuthorMapper {

    @Autowired
    @Qualifier("delegate")
    private AuthorMapper delegate;

    @Autowired
    private BookRepository repository;

    @Override
    public Author toEntity(AuthorDto dto) {
        Author author = delegate.toEntity(dto);

        if (dto.getBookIds() != null) {
            Set<Book> books = dto.getBookIds().stream()
                    .map(bookId -> {
                        Book b = repository.findById(bookId).orElseThrow(() -> new EntityNotFoundException("Book not found with id: " + bookId));
                        b.setAuthor(author);
                        return b;
                    }).collect(Collectors.toSet());
            author.setBooks(books);
        }
        return author;
    }
}
