package com.dawanproject.booktracker.dtos;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@Builder
public class BookDto {

    private long id;
    private String isbn;
    private String title;
    private String publicationDate;
    private int pageNumber;
    private String cover;
    private String author;
    private String category;
    private String description;

}
