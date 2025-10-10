package com.dawanproject.booktracker.dtos;

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

    private long id;
    private String idVolume;
    private String title;
    private LocalDate publicationDate;
    private int pageNumber;
    private String cover;
    private String author;
    private String category;
    private String description;

    @Builder.Default
    private final Set<Review> reviews = new HashSet<>();

}
