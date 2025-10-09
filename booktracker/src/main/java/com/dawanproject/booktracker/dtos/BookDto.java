package com.dawanproject.booktracker.dtos;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

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
    private final List<Long> reviewIds = new ArrayList<>();

}
