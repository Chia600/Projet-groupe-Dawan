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
            String isbn;
            String title;
            String publicationDate;
            int pageNumber;
            String cover;
            String author;
            String category;
            String description;

            for (JsonNode item : items) {
                Optional opt;

                title = item.path("volumeInfo").path("title").asText();
                cover = (item.path("volumeInfo").path("imageLinks").path("thumbnail").asText()).split("&zoom=1")[0];
                pageNumber = item.path("volumeInfo").path("pageCount").asInt();
                publicationDate = (item.path("volumeInfo").path("publishedDate").asText()).split("-")[0];
                description = item.path("volumeInfo").path("description").asText();

                opt = item.path("volumeInfo").path("industryIdentifiers").asOptional();
                if(opt.isEmpty()) {
                    isbn = "";
                } else {
                    isbn = item.path("volumeInfo").path("industryIdentifiers").get(0).path("identifier").asText();
                }

                opt = item.path("volumeInfo").path("authors").asOptional();
                if(opt.isEmpty()) {
                    author = "Auteur Inconnu";
                } else {
                    author = item.path("volumeInfo").path("authors").get(0).asText();
                }

                opt = item.path("volumeInfo").path("categories").asOptional();
                if(opt.isEmpty()) {
                    category = "";
                } else {
                    category = item.path("volumeInfo").path("categories").get(0).asText();
                }

                BookDto bookDto = BookDto.builder()
                        .id(id)
                        .title(title)
                        .author(author)
                        .isbn(isbn)
                        .publicationDate(publicationDate)
                        .cover(cover)
                        .description(description)
                        .pageNumber(pageNumber)
                        .category(category).build();

                bookDtoList.add(bookDto);
                id++;
            }
        }

        return bookDtoList;
    }
}
