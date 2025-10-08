package com.dawanproject.booktracker.tools;

import com.dawanproject.booktracker.dtos.BookDto;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * Classe outil pour fichier JSON
 */
public class JsonTool {

    /**
     * Permet de parcourir le fichier réponse JSON de Google books API pour en récupérer les données utiles
     *
     * @param jsonResults Résultat de la recherche
     * @return List<BookDto> renvoie la liste de bookDto
     * @throws JsonProcessingException
     */
    public static List<BookDto> parseBooksJsonResponse(String jsonResults) throws JsonProcessingException {
        List<BookDto> bookDtoList = new ArrayList<>();
        ObjectMapper mapper = new ObjectMapper();
        long id = 1;

        JsonNode rootNode = mapper.readTree(jsonResults);
        JsonNode items = rootNode.path("items");

        if (items.isEmpty()) {
            BookDto bookDto = getBookDto(rootNode, id);
            bookDtoList.add(bookDto);
        } else {
            for (JsonNode item : items) {
                BookDto bookDto = getBookDto(item, id);
                bookDtoList.add(bookDto);
                id++;
            }
        }

        return bookDtoList;
    }

    private static BookDto getBookDto(JsonNode item, long id) {
        String publicationDate;
        String author;
        String title;
        String idVolume;
        String description;
        String cover;
        String category;
        int pageNumber;
        Optional opt;

        idVolume = item.path("id").asText();
        title = item.path("volumeInfo").path("title").asText();
        cover = (item.path("volumeInfo").path("imageLinks").path("thumbnail").asText()).split("&zoom=1")[0];
        pageNumber = item.path("volumeInfo").path("pageCount").asInt();
        description = item.path("volumeInfo").path("description").asText();

        opt = item.path("volumeInfo").path("publishedDate").asOptional();
        if (opt.isEmpty()) {
            publicationDate = "0000";
        } else {
            publicationDate = (item.path("volumeInfo").path("publishedDate").asText()).split("-")[0];
        }

        opt = item.path("volumeInfo").path("authors").asOptional();
        if (opt.isEmpty()) {
            author = "Auteur Inconnu";
        } else {
            author = item.path("volumeInfo").path("authors").get(0).asText();
        }

        opt = item.path("volumeInfo").path("categories").asOptional();
        if (opt.isEmpty()) {
            category = "";
        } else {
            category = item.path("volumeInfo").path("categories").get(0).asText();
        }

        BookDto bookDto = BookDto.builder()
                .id(id)
                .title(title)
                .author(author)
                .idVolume(idVolume)
                .publicationDate(LocalDate.of(Integer.parseInt(publicationDate), 1, 1))
                .cover(cover)
                .description(description)
                .pageNumber(pageNumber)
                .category(category).build();
        return bookDto;
    }
}
