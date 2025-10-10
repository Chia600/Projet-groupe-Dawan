package com.dawanproject.booktracker.repositories;

import com.dawanproject.booktracker.entities.Book;
import com.dawanproject.booktracker.entities.Category;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface CategoryRepository extends JpaRepository<Category, Long> {
    Optional<Category> findByGenreIgnoreCase(String genre);
}
