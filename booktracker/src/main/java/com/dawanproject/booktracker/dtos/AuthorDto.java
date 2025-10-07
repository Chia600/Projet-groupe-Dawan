package com.dawanproject.booktracker.dtos;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.PositiveOrZero;
import jakarta.validation.constraints.Size;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@ToString
@Builder
public class AuthorDto {

    @PositiveOrZero
    private long id;

    @NotBlank(message = "Le nom est obligatoire")
    @Size(max = 50, message = "Le nom ne doit pas dépasser 50 caractères")
    private String lastname;

    @NotBlank(message = "Le prenom est obligatoire")
    @Size(max = 50, message = "Le prenom ne doit pas dépasser 50 caractères")
    private String firstname;

    @Builder.Default
    private final List<Long> bookIds = new ArrayList<>();
}
