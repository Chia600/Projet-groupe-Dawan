package com.dawanproject.booktracker.entities;

import jakarta.persistence.*;
import lombok.*;
import java.io.Serializable;
import java.time.LocalDate;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Entity
@Table(name = "user_google_book_review")
public class UserGoogleBookReview implements Serializable {

    private static final long serialVersionUID = 1L;

    @EmbeddedId
    private UserGoogleBookReviewPK id;

    @Lob
    @Column(columnDefinition = "TEXT")
    private String review;

    @Column(nullable = true) // ✅ OPTIONNEL maintenant
    private Integer rating; // ✅ Integer au lieu de int pour permettre null

    @Temporal(TemporalType.DATE)
    @Column(nullable = false)
    private LocalDate creationDate;

    public UserGoogleBookReview(Long userId, String googleBookId, String review, Integer rating) {
        this.id = new UserGoogleBookReviewPK(userId, googleBookId);
        this.review = review;
        this.rating = rating;
        this.creationDate = LocalDate.now();
    }
}