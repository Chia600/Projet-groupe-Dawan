package com.dawanproject.booktracker.entities;

import jakarta.persistence.*;
import lombok.*;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.HashSet;
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

    @Version
    private int version;

    @Column(length=150, nullable = false)
    private String title;

    @Lob
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
    @OneToMany(mappedBy = "book")
    private Set<Review> reviews = new HashSet<>();

}
