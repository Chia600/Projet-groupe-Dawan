package com.dawanproject.booktracker.entities;

import jakarta.persistence.*;
import lombok.*;
import java.io.Serializable;
import java.time.LocalDate;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString

@Entity
@Table(name = "review")
public class Review implements Serializable {

    private static final long serialVersionUID = 1L;

    @EmbeddedId
    private ReviewPK reviewId = new ReviewPK();

    @ManyToOne
    @MapsId("userId")
    private User user;

    @ManyToOne
    @MapsId("bookId")
    private Book book;

    @Lob
    private String review;


    private int rating;

    @Temporal(TemporalType.DATE)
    private LocalDate creationDate;


}
