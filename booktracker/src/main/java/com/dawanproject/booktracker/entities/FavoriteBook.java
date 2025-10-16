package com.dawanproject.booktracker.entities;

import jakarta.persistence.*;
import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Entity
@Table(name = "favorite_book")
public class FavoriteBook {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private Long userId;

    @Column(nullable = false)
    private String googleBookId; // L'idVolume de Google Books

    @Column
    private java.time.LocalDateTime addedDate;

    public FavoriteBook(Long userId, String googleBookId) {
        this.userId = userId;
        this.googleBookId = googleBookId;
        this.addedDate = java.time.LocalDateTime.now();
    }
}