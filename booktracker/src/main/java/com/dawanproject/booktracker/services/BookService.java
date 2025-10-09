package com.dawanproject.booktracker.services;

import com.dawanproject.booktracker.dtos.BookDto;

import java.util.List;
import java.util.Optional;

public interface BookService {

    /**
     * Récupère un livre par son ID et renvoie son JSON.
     */
    Optional<BookDto> getBookById(long bookId);


    // Récupérer tous les livres d'une catégorie
    List<BookDto> getBooksByGenre(String genre);

    Optional<List<BookDto>> getBookByTitle(String title);
}
