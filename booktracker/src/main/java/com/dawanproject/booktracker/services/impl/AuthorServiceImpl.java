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

    @Override
    public List<AuthorDto> getAll() {
        List<Author> authors = repository.findAll();
        return authors.stream().map(mapper::toDto).toList();
    }

    @Override
    public Optional<AuthorDto> getAuthorById(long id) {
        return repository.findById(id).map(mapper::toDto);
    }

    @Override
    public Optional<List<AuthorDto>> getAuthorByName(String name) {
        List<Author> authors = repository.findByLastnameLike("%" + name + "%");
        return Optional.of(authors.stream().map(mapper::toDto).toList());
    }

    @Override
    public boolean deleteById(long id) {
        if (repository.existsById(id)) {
            repository.deleteById(id);
            return true;
        }
        return false;
    }

    @Override
    public AuthorDto saveOrUpdate(AuthorDto dto) {
        Author savedAuthor = repository.saveAndFlush(mapper.toEntity(dto));
        return mapper.toDto(savedAuthor);
    }
}
