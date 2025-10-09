package com.dawanproject.booktracker.mappers;

import com.dawanproject.booktracker.dtos.BookDto;
import com.dawanproject.booktracker.entities.Author;
import com.dawanproject.booktracker.entities.Book;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

public abstract class BookMapperDecorator implements BookMapper {

    @Autowired
    @Qualifier("delegate")
    private BookMapper delegate;

    @Override
    public BookDto toDto(Book book) {
        BookDto bookDto = delegate.toDto(book);

        Author author = book.getAuthor();
        bookDto.setAuthor(author.getFirstname() + " " + author.getLastname());

        return bookDto;
    }

    @Override
    public Book toEntity(BookDto bookDto) {
        Book book = delegate.toEntity(bookDto);

        String[] name = bookDto.getAuthor().split(" ");
        Author author = new Author();
        author.setFirstname(name[0]);
        author.setLastname(name[1]);
        book.setAuthor(author);

        return book;
    }
}