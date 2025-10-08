package com.dawanproject.booktracker.controllers;

import com.dawanproject.booktracker.dtos.BookDto;
import com.dawanproject.booktracker.services.GoogleBooksApiService;
import com.dawanproject.booktracker.tools.JsonTool;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;

@WebMvcTest(BookController.class)
@AutoConfigureMockMvc(addFilters = false)
class SearchControllerTest {

    @MockitoBean
    private GoogleBooksApiService service;

    @Autowired
    private MockMvc mockMvc;

    private List<BookDto> bookDtoList;

    @BeforeEach
    public void setUp() {
        Path jsonFilePath = Path.of("src/test/resources/livres.json");
        String jsonResults = null;
        try {
            jsonResults = Files.readString(jsonFilePath);
            bookDtoList = JsonTool.parseBooksJsonResponse(jsonResults);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }

    @Test
    void getAllOk() throws Exception {
        Pageable pageRequest = PageRequest.of(0, 10);
        int start = (int) pageRequest.getOffset();
        int end = Math.min((start + pageRequest.getPageSize()), bookDtoList.size());
        List<BookDto> pageContent = bookDtoList.subList(start, end);

        Page<BookDto> pageable = new PageImpl<>(pageContent, pageRequest, bookDtoList.size());

        when(service.getAll(0, 10, "")).thenReturn(pageable);

        MockHttpServletResponse response = mockMvc.perform(get("/api/books/0/10")
                        .accept(MediaType.APPLICATION_JSON_VALUE)
                        .contentType(MediaType.APPLICATION_JSON_VALUE))
                .andReturn().getResponse();
        assertEquals(200, response.getStatus());

        assertTrue(response.getContentAsString().contains("\"id\":3,\"idVolume\":\"Yq35BY5Fk3gC\",\"title\":\"The Mythical Man-Month\",\"publicationDate\":\"1995-01-01\",\"pageNumber\":348"));
    }
}