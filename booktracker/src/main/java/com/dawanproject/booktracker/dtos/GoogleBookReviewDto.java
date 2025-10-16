package com.dawanproject.booktracker.dtos;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class GoogleBookReviewDto {

    @NotNull(message = "L'ID utilisateur est requis")
    private Long userId;

    @NotNull(message = "L'ID du livre Google est requis")
    private String googleBookId;

    private String review; // Optionnel

    @Min(value = 1, message = "La note doit être au moins 1")
    @Max(value = 5, message = "La note ne peut pas dépasser 5")
    private Integer rating; // ✅ Optionnel (plus de @NotNull)

    private LocalDate creationDate;
}
