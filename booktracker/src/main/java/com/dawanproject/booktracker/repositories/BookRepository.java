package com.dawanproject.booktracker.repositories;

import com.dawanproject.booktracker.entities.Book;
import com.dawanproject.booktracker.entities.Category;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface BookRepository extends JpaRepository<Book, Long> {

    Optional<Book> findByBookId(long bookId);

    List<Book> findByCategoryGenreIgnoreCase(String genre);

    List<Book> findByTitleLikeIgnoreCase(String title);
}