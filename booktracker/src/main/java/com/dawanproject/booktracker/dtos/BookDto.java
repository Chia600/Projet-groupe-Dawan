package com.dawanproject.booktracker.dtos;

import com.dawanproject.booktracker.entities.Review;
import lombok.*;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

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
    private final List<Review> reviews = new ArrayList<>();

}
