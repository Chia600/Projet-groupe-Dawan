package com.dawanproject.booktracker.controllers;

import com.dawanproject.booktracker.dtos.BookDto;
import com.dawanproject.booktracker.services.GoogleBooksApiService;
import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Info;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Optional;

@OpenAPIDefinition(info =
@Info(
        title = "L'api de la recherche",
        description = "SearchAPI"
)
)

@RequiredArgsConstructor

@RestController
@RequestMapping("/api/books")
public class BookController {

    private final GoogleBooksApiService service;

    /**
     * Return 40 computer books if search is empty
     * Else return pagined result of the search
     *
     * @param page the current page
     * @param size number of books by page
     * @param optional search criteria
     * @return ResponseEntity containing list of books
     * @throws Exception
     */
    @GetMapping(value = {"/{page}/{size}/{search}", "/{page}/{size}"}, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Page<BookDto>> getAll(@PathVariable int page, @PathVariable int size,
                                                @PathVariable(value = "search", required = false) Optional<String> optional
    ) throws Exception {

        String search;
        if (optional.isPresent()) {
            search = optional.get();
        } else {
            search = "";
        }

        Page<BookDto> customerPage = service.getAll(page, size, search);
        HttpHeaders headers = new HttpHeaders();
        headers.add("X-Page-Number", String.valueOf(customerPage.getNumber()));
        headers.add("X-Page-Size", String.valueOf(customerPage.getSize()));
        headers.add("X-Total-Elements", String.valueOf(customerPage.getTotalElements()));

        return ResponseEntity.ok()
                .headers(headers)
                .body(customerPage);
    }
}
