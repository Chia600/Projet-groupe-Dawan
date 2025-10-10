package com.dawanproject.booktracker.services.impl;

import com.dawanproject.booktracker.dtos.BookDto;
import com.dawanproject.booktracker.entities.Book;
import com.dawanproject.booktracker.mappers.BookMapper;
import com.dawanproject.booktracker.repositories.BookRepository;
import com.dawanproject.booktracker.services.BookService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class BookServiceImpl implements BookService {

    private final BookRepository bookRepository;
    private final BookMapper mapper;

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
}