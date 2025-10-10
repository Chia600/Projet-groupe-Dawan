package com.dawanproject.booktracker.dtos;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PastOrPresent;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

/**
 * DTO for representing Review data for creation, update, and retrieval.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ReviewDto {

    /**
     * Composite ID for the review (userId and bookId).
     */
    @NotNull(message = "L'ID utilisateur est requis")
    private Long userId;

    @NotNull(message = "L'ID du livre est requis")
    private Long bookId;

    /**
     * Review text.
     */
    private String review;

    /**
     * Rating of the review (1 to 5).
     */
    @NotNull(message = "La note est requise")
    @Min(value = 1, message = "La note doit être au moins 1")
    @Max(value = 5, message = "La note ne peut pas dépasser 5")
    private Integer rating;

    /**
     * Date the review was created.
     */
    @PastOrPresent(message = "La date de création doit être dans le passé ou aujourd'hui")
    private LocalDate creationDate;
}