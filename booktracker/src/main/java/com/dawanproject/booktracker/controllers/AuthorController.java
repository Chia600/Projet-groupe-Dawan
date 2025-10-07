package com.dawanproject.booktracker.controllers;

import com.dawanproject.booktracker.dtos.AuthorDto;
import com.dawanproject.booktracker.mappers.AuthorMapper;
import com.dawanproject.booktracker.services.AuthorService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequiredArgsConstructor

@RestController
@Validated
@RequestMapping("/api/authors")
public class AuthorController {

    private final AuthorService service;

    private final AuthorMapper mapper;

    /**
     * Return all authors
     *
     * @return ResponseEntity containing all authors
     */
    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<AuthorDto>> getAll() {
        return ResponseEntity.ok(service.getAll());
    }

    /**
     * Retrieves author by id
     *
     * @param id The ID of the author
     * @return ResponseEntity containing the author if found or HTTP status 404 (Not Found).
     */
    @GetMapping(value = "/{id:[0-9]+}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<AuthorDto> getById(@PathVariable long id) {
        return service.getAuthorById(id)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    /**
     * Retrieve list of authors by name
     *
     * @param name author name
     * @return List<AuthorDto>
     */
    @GetMapping(value = "/name/{name:[a-zA-Z ]+}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<AuthorDto>> getByName(@PathVariable String name) {
        return service.getAuthorByName(name)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    /**
     * Delete an author
     *
     * @param id author id
     * @return True if deleted, false if not found
     */
    @DeleteMapping(value = "/{id}")
    public ResponseEntity<Void> delete(@PathVariable long id) {
        boolean deleted = service.deleteById(id);
        if (deleted) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.notFound().build();
    }

    /**
     * Creates a new author
     *
     * @param dto The author data to create.
     * @return The created AuthorDto.
     */
    @ResponseStatus(code = HttpStatus.CREATED)
    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<AuthorDto> saveOrUpdate(@Valid @RequestBody AuthorDto dto) {
        return ResponseEntity.status(HttpStatus.OK).body(service.saveOrUpdate(dto));
    }

}
