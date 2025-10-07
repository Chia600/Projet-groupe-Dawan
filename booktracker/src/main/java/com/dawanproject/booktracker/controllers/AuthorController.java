package com.dawanproject.booktracker.controllers;

import com.dawanproject.booktracker.dtos.AuthorDto;
import com.dawanproject.booktracker.mappers.AuthorMapper;
import com.dawanproject.booktracker.services.AuthorService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequiredArgsConstructor

@RestController
@RequestMapping("/api/authors")
public class AuthorController {

    private final AuthorService service;

    private final AuthorMapper mapper;

    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public List<AuthorDto> getAll() {
        return service.getAll(Pageable.unpaged()).getContent();
    }

    @GetMapping(params = {"page", "size"}, produces = MediaType.APPLICATION_JSON_VALUE)
    public List<AuthorDto> getAll(Pageable page) {
        return service.getAll(page).getContent();
    }

    @GetMapping(value = "/{id:[0-9]+}", produces = MediaType.APPLICATION_JSON_VALUE)
    public AuthorDto getById(@PathVariable long id) {
        return service.getById(id);
    }

    @GetMapping(value = "/{nom:[a-zA-Z ]+}", produces = MediaType.APPLICATION_JSON_VALUE)
    public List<AuthorDto> getByName(@PathVariable String name) {
        return service.getByName(name);
    }

    @DeleteMapping(value = "/{id}", produces = MediaType.TEXT_PLAIN_VALUE)
    public String deleteById(@PathVariable long id) {
        service.deleteById(id);
        return String.format("L'id=%d est supprimé", id);
    }

    @ResponseStatus(code = HttpStatus.CREATED)
    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public AuthorDto create(@Valid @RequestBody AuthorDto dto) {
        return service.create(dto);
    }

//    @GetMapping("/{id}/books")
//    public ResponseEntity<List<Book>> getBooksByAuthor(@PathVariable Long id) {
//        AuthorDto author = service.getById(id);
//        List<Book> books = bookService.findByAuthor(id);
//        return ResponseEntity.ok(books);
//    }
}
