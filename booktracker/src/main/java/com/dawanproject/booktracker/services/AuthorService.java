package com.dawanproject.booktracker.services;

import com.dawanproject.booktracker.dtos.AuthorDto;

import java.util.List;
import java.util.Optional;

public interface AuthorService {

    List<AuthorDto> getAll();

    Optional<AuthorDto> getAuthorById(long id);

    Optional<List<AuthorDto>> getAuthorByName(String name);

    boolean deleteById(long id);

    AuthorDto saveOrUpdate(AuthorDto dto);
}
