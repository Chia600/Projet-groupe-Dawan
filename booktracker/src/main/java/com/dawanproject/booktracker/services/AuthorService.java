package com.dawanproject.booktracker.services;

import com.dawanproject.booktracker.dtos.AuthorDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface AuthorService {

    Page<AuthorDto> getAll(Pageable page);

    AuthorDto getById(long id);

    List<AuthorDto> getByName(String name);

    void deleteById(long id);

    AuthorDto create(AuthorDto dto);
}
