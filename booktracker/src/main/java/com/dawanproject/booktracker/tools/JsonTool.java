package com.dawanproject.booktracker.tools;

import com.dawanproject.booktracker.dtos.BookDto;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;

/**
 * Classe outil pour fichier JSON
 */
public class JsonTool {

    /**
     * Permet de parcourir le fichier réponse JSON de Google books API pour en récupérer les données utiles
     *
     * @param jsonMappedResponse Map des résultats de la recherche (<num page>,<résultats>)
     * @return List<BookDto> renvoie la liste de bookDto
     * @throws JsonProcessingException
     */
    public static List<BookDto> parseBooksJsonResponse(Map<Integer, String> jsonMappedResponse) throws JsonProcessingException {
        List<BookDto> bookDtoList = new ArrayList<>();
        ObjectMapper mapper = new ObjectMapper();
        long id = 1;

        for (var entry : jsonMappedResponse.entrySet()) {
            JsonNode rootNode = mapper.readTree(entry.getValue());
            JsonNode items = rootNode.path("items");

            for (JsonNode item : items) {
                BookDto bookDto = new BookDto();
                Optional opt;

                bookDto.setId(id);
                bookDto.setTitle(item.path("volumeInfo").path("title").asText());

                opt = item.path("volumeInfo").path("industryIdentifiers").asOptional();
                if(opt.isEmpty()) {
                    bookDto.setIsbn("");
                } else {
                    bookDto.setIsbn(item.path("volumeInfo").path("industryIdentifiers").get(0).path("identifier").asText());
                }

                bookDto.setCover((item.path("volumeInfo").path("imageLinks").path("thumbnail").asText()).split("&zoom=1")[0]);
                bookDto.setPageNumber(item.path("volumeInfo").path("pageCount").asInt());
                bookDto.setPublicationDate((item.path("volumeInfo").path("publishedDate").asText()).split("-")[0]);

                opt = item.path("volumeInfo").path("authors").asOptional();
                if(opt.isEmpty()) {
                    bookDto.setAuthor("Auteur Inconnu");
                } else {
                    bookDto.setAuthor(item.path("volumeInfo").path("authors").get(0).asText());
                }

                opt = item.path("volumeInfo").path("categories").asOptional();
                if(opt.isEmpty()) {
                    bookDto.setCategory("");
                } else {
                    bookDto.setCategory(item.path("volumeInfo").path("categories").get(0).asText());
                }

                bookDto.setDescription(item.path("volumeInfo").path("description").asText());

                bookDtoList.add(bookDto);
                id++;
            }
        }

        return bookDtoList;
    }
}
