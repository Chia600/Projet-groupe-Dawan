package com.dawanproject.booktracker.dtos;

import lombok.*;

@Getter
@Setter
@ToString
@Builder
public class BookDto {

    private long id;
    private String idVolume;
    private String title;
    private String publicationDate;
    private int pageNumber;
    private String cover;
    private String author;
    private String category;
    private String description;

}
