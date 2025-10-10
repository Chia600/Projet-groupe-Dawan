package com.dawanproject.booktracker.entities;

import com.fasterxml.jackson.annotation.JsonBackReference;
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
    @JsonBackReference
    private User user;

    @ManyToOne
    @MapsId("bookId")
    @JsonBackReference
    private Book book;

    @Lob
    private String review;


    private int rating;

    @Temporal(TemporalType.DATE)
    private LocalDate creationDate;


}
