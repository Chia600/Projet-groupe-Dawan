package com.dawanproject.booktracker.controllers;

import com.dawanproject.booktracker.dtos.BookDto;
import com.dawanproject.booktracker.mappers.BookMapper;
import com.dawanproject.booktracker.services.GoogleBooksApiService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/booktracker/api")
public class BookController {

    private final GoogleBooksApiService googleBooksApiService;

    @GetMapping("/details/{bookId}")
    public ResponseEntity<BookDto> getBookDetails(@PathVariable String bookId) throws Exception {

        return ResponseEntity.ok(googleBooksApiService.getBookById(bookId));
    }
}
