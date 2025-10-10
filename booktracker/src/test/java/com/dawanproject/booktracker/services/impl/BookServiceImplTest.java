package com.dawanproject.booktracker.services.impl;

import com.dawanproject.booktracker.dtos.BookDto;
import com.dawanproject.booktracker.entities.Book;
import com.dawanproject.booktracker.mappers.BookMapper;
import com.dawanproject.booktracker.repositories.BookRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.*;

@DisplayName("Tests unitaires du service BookServiceImpl")
class BookServiceImplTest {

    @Mock
    private BookRepository bookRepository;

    @Mock
    private BookMapper bookMapper;

    @InjectMocks
    private BookServiceImpl bookService;

    private Book book;
    private BookDto bookDto;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        // Données de test
        book = new Book();
        book.setBookId(1L);
        book.setTitle("Test Book");

        // DTO correspondant
        bookDto = BookDto.builder()
                .id(1L)
                .title("Test Book")
                .build();
    }

    @Test
    @DisplayName("Doit retourner un BookDto quand le livre existe")
    void shouldReturnBookDto_whenBookExists() {
        // Arrange
        when(bookRepository.findById(1L)).thenReturn(Optional.of(book));
        when(bookMapper.toDto(book)).thenReturn(bookDto);

        // Act
        Optional<BookDto> result = bookService.getBookById(1L);

        // Assert
        assertTrue(result.isPresent(), "Le résultat ne doit pas être vide");
        assertEquals("Test Book", result.get().getTitle());
        assertEquals(1L, result.get().getId());
        verify(bookRepository, times(1)).findById(1L);
        verify(bookMapper, times(1)).toDto(book);
    }

    @Test
    @DisplayName("Doit retourner un Optional vide quand le livre n'existe pas")
    void shouldReturnEmptyOptional_whenBookNotExists() {
        // Arrange
        when(bookRepository.findById(99L)).thenReturn(Optional.empty());

        // Act
        Optional<BookDto> result = bookService.getBookById(99L);

        // Assert
        assertTrue(result.isEmpty(), "Le résultat doit être vide si aucun livre n'est trouvé");
        verify(bookRepository, times(1)).findById(99L);
        verifyNoInteractions(bookMapper);
    }
}
