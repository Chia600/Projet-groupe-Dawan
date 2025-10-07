package com.dawanproject.booktracker.repositories;

import com.dawanproject.booktracker.entities.Author;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface AuthorRepository extends JpaRepository<Author, Long> {

    List<Author> findByNameLike(String name);

    int removeById(long id);
}
