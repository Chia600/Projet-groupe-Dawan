package com.dawanproject.booktracker.dtos;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.PastOrPresent;
import jakarta.validation.constraints.PositiveOrZero;
import jakarta.validation.constraints.Size;
import lombok.*;

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
    private String publicationDate;

    private int pageNumber;

    private String cover;

    private String author;

    private String category;

    private String description;

}
