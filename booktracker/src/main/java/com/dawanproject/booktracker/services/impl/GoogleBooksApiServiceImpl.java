package com.dawanproject.booktracker.services.impl;

import com.dawanproject.booktracker.dtos.BookDto;
import com.dawanproject.booktracker.services.IGoogleBooksApiService;
import com.dawanproject.booktracker.tools.JsonTool;
import com.fasterxml.jackson.core.JsonProcessingException;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Service d'appel à l'API Google Books
 */
@Service
public class GoogleBooksApiServiceImpl implements IGoogleBooksApiService {

    private static final String GOOGLE_BOOK_API_URL = "https://www.googleapis.com/books/v1/volumes?q=";
    private static final String GOOGLE_BOOK_API_KEY = "AIzaSyCbJZJyMK5-BDVBiCTBXzykJBlDzEKMSkA";

    /**
     * Permet d'effectuer une recherche de livres via l'API Google Books
     * (si le paramètre search est vide, récupère 100 livres)
     *
     * @param page numéro de la page courante
     * @param size nombre de résultats affichés par page
     * @param search critères de recherche
     * @return List<BookDto> renvoie la liste de bookDto
     * @throws JsonProcessingException
     */
    @Override
    public List<BookDto> getAllBy(int page, int size, String search) throws JsonProcessingException { //Récupération de 100 livres
        int i = -1;
        int p = 1;
        Map<Integer, String> response = new HashMap<>();

        RestClient rc = RestClient.create();

        while(i<100) {
            String urlString = GOOGLE_BOOK_API_URL + "2025+intitle:le+la+et&key=" + GOOGLE_BOOK_API_KEY + "&printType=books&orderBy=newest&maxResults=10&startIndex=" + (i+1);
            String res = rc.get().uri(urlString).retrieve().body(String.class);
            response.put(p, res);
            i+=11;
            p++;
        }

        return JsonTool.parseBooksJsonResponse(response);
    }
}
