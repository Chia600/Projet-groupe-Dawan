package com.dawanproject.booktracker.entities;

import jakarta.persistence.*;
import lombok.*;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.Set;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString

@Entity
@Table(name = "book")
public class Book implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long bookId;

    @Column(nullable = false)
    private String isbn;

    @Column(length=50, nullable = false)
    private String title;

    private String description;

    private int pageNumber;

    @ToString.Exclude
    private String cover;

    @Temporal(TemporalType.DATE)
    private LocalDate publicationDate;

    @ManyToOne
    private Author author;

    @ManyToOne
    private Category category;

    @ToString.Exclude
    @OneToMany
    private Set<Review> reviews;

}
