package com.dawanproject.booktracker.services.impl;

import com.dawanproject.booktracker.dtos.BookDto;
import com.dawanproject.booktracker.services.BookService;
import com.dawanproject.booktracker.services.GoogleBooksApiService;
import com.dawanproject.booktracker.tools.JsonTool;
import com.fasterxml.jackson.core.JsonProcessingException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * Service d'appel à l'API Google Books
 */
@Service
public class GoogleBooksApiServiceImpl implements GoogleBooksApiService {

    @Value("${google.books.api.url.base}")
    private String googleBookApiUrl;

    @Value("${google.books.api.key}")
    private String googleBookApiKey;

    private BookService bookService;

    @Autowired
    public GoogleBooksApiServiceImpl(BookService bookService) {
        this.bookService = bookService;
    }

    /**
     * Recherche paginée de livres via l'API Google Books
     * (si le paramètre search est vide, récupère des livres ayant pour genre 'Computers')
     *
     * @param page   page courante
     * @param size   nombre d'éléments par page
     * @param search critères de recherche
     * @return Page<BookDto>
     * @throws JsonProcessingException
     */
    @Override
    public Page<BookDto> getAll(int page, int size, String search) throws JsonProcessingException {
        RestClient rc = RestClient.create();
        if (search.trim().isEmpty()) {
            search = "subject:Computers";
        } else {
            search = search.contains(" ") ? search.replaceAll(" ", "+") : search;
        }

        //Appel de l'API Google Books
        String urlString = googleBookApiUrl + "?q=" + search + "&key=" + googleBookApiKey + "&maxResults=40&orderBy=relevance";
        String results = rc.get().uri(urlString).retrieve().body(String.class);

        List<BookDto> bookDtoList = JsonTool.parseBooksJsonResponse(results);

        //Conversion de List<BookDto> en Page<bookDto>
        Pageable pageRequest = PageRequest.of(page, size);
        int start = (int) pageRequest.getOffset();
        int end = Math.min((start + pageRequest.getPageSize()), bookDtoList.size());
        List<BookDto> pageContent = bookDtoList.subList(start, end);

        return new PageImpl<>(pageContent, pageRequest, bookDtoList.size());

    }

    /**
     * Nouvelle méthode : récupère un seul livre via l’API Google
     *
     * @param googleBookId Id d'un livre
     * @return BookDto
     * @throws JsonProcessingException
     */
    @Override
    public BookDto getBookById(String googleBookId) throws JsonProcessingException {
        String url = googleBookApiUrl + "/" + googleBookId;
        RestClient rc = RestClient.create();

        //Récupération du livre via Google Books API par son IdVolume
        String response = rc.get().uri(url).retrieve().body(String.class);
        List<BookDto> booksApiList = JsonTool.parseBooksJsonResponse(response);
        BookDto bookApi = booksApiList.getFirst();
        String title = bookApi.getTitle();

        //Récupération du livre par son titre en base de données, s'il existe
        Optional<List<BookDto>> optBooklist = bookService.getBookByTitle(title);
        List<BookDto> booklistDB = optBooklist.orElse(new ArrayList<>());

        //Si le livre est en base de données, on le renvoie (contient les données de reviews et rating)
        //sinon on retourne le livre récupéré dans Google Books API (pas de reviews ou rating)
        if (!booklistDB.isEmpty())
            bookApi = booklistDB.getFirst();
        return bookApi;
    }
}