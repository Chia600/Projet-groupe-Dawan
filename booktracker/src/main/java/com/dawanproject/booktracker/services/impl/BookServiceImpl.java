package com.dawanproject.booktracker.services.impl;

import com.dawanproject.booktracker.dtos.AuthorDto;
import com.dawanproject.booktracker.dtos.BookDto;
import com.dawanproject.booktracker.entities.Book;
import com.dawanproject.booktracker.entities.Category;
import com.dawanproject.booktracker.entities.Role;
import com.dawanproject.booktracker.entities.User;
import com.dawanproject.booktracker.enums.RoleName;
import com.dawanproject.booktracker.mappers.AuthorMapper;
import com.dawanproject.booktracker.mappers.BookMapper;
import com.dawanproject.booktracker.repositories.BookRepository;
import com.dawanproject.booktracker.services.AuthorService;
import com.dawanproject.booktracker.services.BookService;
import com.dawanproject.booktracker.services.CategoryService;
import com.dawanproject.booktracker.services.GoogleBooksApiService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional
public class BookServiceImpl implements BookService {

    private final BookRepository bookRepository;
    private final BookMapper mapper;
    private final AuthorService authorService;
    private final CategoryService categoryService;
    private final AuthorMapper authorMapper;

    /**
     * Récupère un livre par son ID et le convertit en JSON
     */
    @Override
    public Optional<BookDto> getBookById(long bookId) {
        return bookRepository.findById(bookId).map(mapper::toDto);
    }

    // Récupérer tous les livres d'une catégorie
    @Override
    public List<BookDto> getBooksByGenre(String genre) {
        return bookRepository.findByCategoryGenreIgnoreCase(genre)
                .stream()
                .map(mapper::toDto)
                .toList();
    }

    /**
     * Récupère les livres dont le titre correspond au critère LIKE spécifié.
     *
     * @param title titre du livre (peut être partiel)
     * @return une liste de {@link BookDto} encapsulée dans un {@link Optional}
     */
    @Override // 2 usages new
    public Optional<List<BookDto>> getBookByTitle(String title) {
        List<Book> books = bookRepository.findByTitleLikeIgnoreCase("%" + title + "%");

        if (books.isEmpty())
            return Optional.of(new ArrayList<>());

        if (books.size() == 1)
            return Optional.of(List.of(mapper.toDto(books.getFirst())));

        return Optional.of(books.stream().map(mapper::toDto).toList());
    }

    @Override
    public Optional<BookDto> createBook(BookDto bookApi) throws Exception {

        if (bookApi.getIdVolume() == null) {
            return Optional.empty();
        }

        Book book = mapper.toEntity(bookApi);
        AuthorDto authorDto = authorService.saveOrUpdate(authorMapper.toDto(book.getAuthor()));
        Category category = categoryService.createCategory(book.getCategory());
        book.setCategory(category);
        book.setAuthor(authorMapper.toEntity(authorDto));
        Book savedBook = bookRepository.save(book);

        return Optional.of(mapper.toDto(savedBook));
    }
}