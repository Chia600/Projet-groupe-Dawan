package com.dawanproject.booktracker.services.impl;

import com.dawanproject.booktracker.dtos.BookDto;
import com.dawanproject.booktracker.services.BookService;
import com.dawanproject.booktracker.tools.JsonTool;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.test.util.ReflectionTestUtils;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;


@ExtendWith(MockitoExtension.class)
class GoogleBooksApiServiceImplTest {

    private static final String URL_VALUE = "https://www.googleapis.com/books/v1/volumes";
    private static final String KEY_VALUE = "AIzaSyCbJZJyMK5-BDVBiCTBXzykJBlDzEKMSkA";

    private GoogleBooksApiServiceImpl service;

    private BookService bookService;

    @BeforeEach
    void setUp() {
        service = new GoogleBooksApiServiceImpl(bookService);

        ReflectionTestUtils.setField(
                service,
                "googleBookApiUrl",
                URL_VALUE);
        ReflectionTestUtils.setField(
                service,
                "googleBookApiKey",
                KEY_VALUE);
    }

    @Test
    public void getAllLivresSearchEmptyTest() throws IOException {
        String search = "";

        Path jsonFilePath = Path.of("src/test/resources/livres.json");
        String jsonResults = Files.readString(jsonFilePath);

        List<BookDto> bookDtoList = JsonTool.parseBooksJsonResponse(jsonResults);

        Page<BookDto> result = service.getAll(0, 10, search);

        assertEquals(40, bookDtoList.size());
        assertEquals(result.stream().toList().get(0).getId(), bookDtoList.get(0).getId());
        assertEquals(result.stream().toList().get(0).getAuthor(), bookDtoList.get(0).getAuthor());
        assertEquals(result.stream().toList().get(5).getId(), bookDtoList.get(5).getId());
    }

    @Test
    public void getAllLivresSearchValuedTest() throws IOException {
        String search1 = "inauthor:Stephen King"; //recherche google API uniquement sur l'auteur
        String search2 = "Java";

        Page<BookDto> result1 = service.getAll(0, 10, search1);
        Page<BookDto> result2 = service.getAll(0, 10, search2);

        assertEquals(result1.stream().toList().get(0).getAuthor(), "Stephen King");
        assertEquals(result1.stream().toList().get(3).getAuthor(), "Stephen King");
        assertEquals(result1.stream().toList().get(7).getAuthor(), "Stephen King");

        assertTrue(result2.stream().toList().get(0).getTitle().contains(search2));
        assertTrue(result2.stream().toList().get(2).getTitle().contains(search2));
        assertTrue(result2.stream().toList().get(5).getTitle().contains(search2));
        assertTrue(result2.stream().toList().get(6).getTitle().contains(search2));
        assertTrue(result2.stream().toList().get(8).getTitle().contains(search2));
    }

}