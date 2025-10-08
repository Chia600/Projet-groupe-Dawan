package com.dawanproject.booktracker.dtos;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.PastOrPresent;
import jakarta.validation.constraints.PositiveOrZero;
import jakarta.validation.constraints.Size;
import com.dawanproject.booktracker.entities.Review;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;


@Getter
@Setter
@ToString
@Builder
public class BookDto {

    @PositiveOrZero
    private long id;

    private String idVolume;

    @NotBlank(message = "Le nom est obligatoire")
    @Size(max = 150, message = "Le titre ne doit pas dépasser 150 caractères")
    private String title;

    @PastOrPresent
    private LocalDate publicationDate;
    private int pageNumber;

    private String cover;

    private String author;

    private String category;

    private String description;

    @Builder.Default
    private final Set<Review> reviews = new HashSet<>();

}
