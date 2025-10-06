package com.dawanproject.booktracker.services.impl;

import com.dawanproject.booktracker.dtos.BookDto;
import com.dawanproject.booktracker.services.GoogleBooksApiService;
import com.dawanproject.booktracker.tools.JsonTool;
import com.fasterxml.jackson.core.JsonProcessingException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Service d'appel à l'API Google Books
 */
@Service
public class GoogleBooksApiServiceImpl implements GoogleBooksApiService {

    @Value("${google.books.api.url.base}")
    private String googleBookApiUrl;

    @Value("${google.books.api.key}")
    private String googleBookApiKey;

    /**
     * Recherche paginée de livres via l'API Google Books
     * (si le paramètre search est vide, récupère des livres ayant pour genre 'Computers')
     *
     * @param page page courante
     * @param size nombre d'éléments par page
     * @param search critères de recherche
     * @return Page<BookDto>
     * @throws JsonProcessingException
     */
    @Override
    public Page<BookDto> getAllLivres(int page, int size, String search) throws JsonProcessingException {
        RestClient rc = RestClient.create();
        if(search.trim().isEmpty()) {
            search = "subject:Computers";
        } else {
            search = search.contains(" ") ? search.replaceAll(" ", "+") : search;
        }

        //Appel de l'API Google Books
        String urlString = googleBookApiUrl + search + "&key=" + googleBookApiKey + "&maxResults=40";
        String results = rc.get().uri(urlString).retrieve().body(String.class);

        List<BookDto> bookDtoList = JsonTool.parseBooksJsonResponse(results);

        //Conversion de List<BookDto> en Page<bookDto>
        Pageable pageRequest = PageRequest.of(page, size);
        int start = (int) pageRequest.getOffset();
        int end = Math.min((start + pageRequest.getPageSize()), bookDtoList.size());
        List<BookDto> pageContent = bookDtoList.subList(start, end);

        return new PageImpl<>(pageContent, pageRequest, bookDtoList.size());

    }
}
