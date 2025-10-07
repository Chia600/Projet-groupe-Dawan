package com.dawanproject.booktracker.repositories;

import com.dawanproject.booktracker.entities.Book;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface BookRepository extends JpaRepository<Book, Long> {

    Optional<Book> findByBookId(long bookId);

    List<Book> findByCategory_CategoryId(Long categoryId);
}