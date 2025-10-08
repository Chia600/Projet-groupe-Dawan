package com.dawanproject.booktracker.services.impl;

import com.dawanproject.booktracker.dtos.AuthorDto;
import com.dawanproject.booktracker.entities.Author;
import com.dawanproject.booktracker.mappers.AuthorMapper;
import com.dawanproject.booktracker.repositories.AuthorRepository;
import com.dawanproject.booktracker.services.AuthorService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor

@Service
@Transactional
public class AuthorServiceImpl implements AuthorService {

    private final AuthorRepository repository;

    private final AuthorMapper mapper;

    /**
     * Retrieves all authors.
     *
     * @return List of all UserDTOs.
     */
    @Override
    public List<AuthorDto> getAll() {
        List<Author> authors = repository.findAll();
        return authors.stream().map(mapper::toDto).toList();
    }

    /**
     * Retrieves a user by their ID.
     *
     * @param id The ID of the user.
     * @return Optional containing the UserDTO if found, empty otherwise.
     */
    @Override
    public Optional<AuthorDto> getAuthorById(long id) {
        return repository.findById(id).map(mapper::toDto);
    }

    /**
     * Retrieve authors with name like name criteria
     *
     * @param name author name
     * @return List<AuthorDto>
     */
    @Override
    public Optional<List<AuthorDto>> getAuthorByName(String name) {
        List<Author> authors = repository.findByLastnameLike("%" + name + "%");
        return Optional.of(authors.stream().map(mapper::toDto).toList());
    }

    /**
     * Delete author by its id
     *
     * @param id author id
     * @return True if deleted, false if not found.
     */
    @Override
    public boolean deleteById(long id) {
        if (repository.existsById(id)) {
            repository.deleteById(id);
            return true;
        }
        return false;
    }

    /**
     * Creates a new author
     *
     * @param dto The author data to create.
     * @return The created AuthorDto.
     */
    @Override
    public AuthorDto saveOrUpdate(AuthorDto dto) {
        Author savedAuthor = repository.saveAndFlush(mapper.toEntity(dto));
        return mapper.toDto(savedAuthor);
    }
}
