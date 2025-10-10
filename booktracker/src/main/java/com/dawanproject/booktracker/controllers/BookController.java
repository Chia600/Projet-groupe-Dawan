package com.dawanproject.booktracker.controllers;

import com.dawanproject.booktracker.dtos.BookDto;
import com.dawanproject.booktracker.services.BookService;
import com.dawanproject.booktracker.services.GoogleBooksApiService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * Contrôleur principal pour la gestion des livres et de leurs données associées.
 * <p>
 * Cette classe permet :
 * <ul>
 *     <li>De récupérer les détails d’un livre depuis l’API Google Books</li>
 *     <li>De consulter les reviews stockées en base de données pour un livre local</li>
 * </ul>
 * <p>
 * Les appels à la base de données sont effectués via {@link BookService},
 * tandis que les données externes proviennent de {@link GoogleBooksApiService}.
 */
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/books")
public class BookController {

    private final GoogleBooksApiService googleBooksApiService;
    private final BookService bookService;

    /**
     * Récupère les détails d’un livre à partir de l’API Google Books.
     *
     * @param bookId identifiant du livre dans l’API Google Books
     * @return un {@link BookDto} contenant les informations détaillées du livre
     * @throws Exception en cas d’erreur de communication ou de parsing JSON
     */
    @GetMapping("/details/{bookId}")
    public ResponseEntity<BookDto> getBookDetails(@PathVariable String bookId) throws Exception {
        return ResponseEntity.ok(googleBooksApiService.getBookById(bookId)); //QbUACwAAQBAJ

    }

    // Récupérer tous les livres d'une catégorie
    @GetMapping("/category/{genre}")
    public ResponseEntity<List<BookDto>> getBooksByGenre(@PathVariable String genre) {
        List<BookDto> books = bookService.getBooksByGenre(genre);
        if (books.isEmpty()) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(books);
    }

    /**
     * Récupère les livres dont le titre correspond (partiellement ou totalement)
     * au nom indiqué dans l'URL.
     * <p>
     * Exemple :
     * GET /api/books/title/Misery
     *
     * @param title le titre ou une partie du titre du livre recherché
     * @return une liste de {@link BookDto} si trouvée, sinon 404 Not Found
     */
    @GetMapping(
            value = "/title/{title:[a-zA-ZÀ-ÿ0-9 '\\-]+}",
            produces = org.springframework.http.MediaType.APPLICATION_JSON_VALUE
    )
    public ResponseEntity<List<BookDto>> getBooksByTitle(@PathVariable String title) {
        return bookService.getBookByTitle(title)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }


}
