package com.dawanproject.booktracker.services.impl;

import com.dawanproject.booktracker.dtos.AuthorDto;
import com.dawanproject.booktracker.entities.Author;
import com.dawanproject.booktracker.mappers.AuthorMapper;
import com.dawanproject.booktracker.repositories.AuthorRepository;
import com.dawanproject.booktracker.services.AuthorService;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@RequiredArgsConstructor

@Service
@Transactional
public class AuthorServiceImpl implements AuthorService {

    private final AuthorRepository repository;

    private final AuthorMapper mapper;

    @Override
    public Page<AuthorDto> getAll(Pageable page) {
        Page<Author> authors = repository.findAll(page);
        return authors.map(a -> mapper.toDto(a));
    }

    @Override
    public AuthorDto getById(long id) {
        Author author = repository.findById(id).orElseThrow(() -> new EntityNotFoundException("Author not found with id: " + id));
        return mapper.toDto(author);
    }

    @Override
    public List<AuthorDto> getByName(String name) {
        List<Author> authors = repository.findByNameLike("%" + name + "%");
        return authors.stream().map(a -> mapper.toDto(a)).toList();
    }

    @Override
    public void deleteById(long id) {
        if (repository.removeById(id) == 0) {
            throw new EntityNotFoundException("Author not found with id: " + id);
        }
    }

    @Override
    public AuthorDto create(AuthorDto dto) {
        return mapper.toDto(repository.save(mapper.toEntity(dto)));
    }
}
