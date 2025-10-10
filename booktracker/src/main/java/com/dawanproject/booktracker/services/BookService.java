package com.dawanproject.booktracker.services;

import com.dawanproject.booktracker.dtos.BookDto;

import java.util.List;
import java.util.Optional;

public interface BookService {

    /**
     * Récupère un livre par son ID
     *
     * @param bookId id du livre recherché
     * @return Optional<BookDto>
     */
    Optional<BookDto> getBookById(long bookId);

    /**
     * Récupère une liste de livres selon une categorie donnée (genre).
     *
     * @param genre genre recherché
     * @return List<BookDto>
     */
    List<BookDto> getBooksByGenre(String genre);

    /**
     * Récupère une liste de livres selon le titre.
     *
     * @param title titre recherché
     * @return Optional<List < BookDto>>
     */
    Optional<List<BookDto>> getBookByTitle(String title);
}
