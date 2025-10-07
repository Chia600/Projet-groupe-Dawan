package com.dawanproject.booktracker.services.impl;

import com.dawanproject.booktracker.entities.Book;
import com.dawanproject.booktracker.repositories.BookRepository;
import com.dawanproject.booktracker.services.BookService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class BookServiceImpl implements BookService {

    private final BookRepository bookRepository;
    private final ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public String getBookById(long bookId) {
        Book book = bookRepository.findByBookId(bookId)
                .orElseThrow(() -> new EntityNotFoundException("Book not found with id: " + bookId));

        try {
            return objectMapper.writeValueAsString(book); // 🔹 transforme l’entité Book en JSON
        } catch (JsonProcessingException e) {
            throw new RuntimeException("Erreur de conversion Book -> JSON", e);
        }
    }
}
