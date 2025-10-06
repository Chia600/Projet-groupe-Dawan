package com.dawanproject.booktracker.controller;

import com.dawanproject.booktracker.entities.Review;
import com.dawanproject.booktracker.entities.ReviewPK;
import com.dawanproject.booktracker.repositories.ReviewRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/reviews")
@Validated
public class ReviewController {

    @Autowired
    private ReviewRepository reviewRepository;

    // Créer une critique
    @PostMapping
    public ResponseEntity<Review> createReview(@Valid @RequestBody Review review) {
        Review savedReview = reviewRepository.save(review);
        return ResponseEntity.status(201).body(savedReview);
    }

    // Récupérer toutes les critiques
    @GetMapping
    public ResponseEntity<List<Review>> getAllReviews() {
        List<Review> reviews = reviewRepository.findAll();
        return ResponseEntity.ok(reviews);
    }

    // Récupérer une critique par ID (userId et bookId)
    @GetMapping("/{userId}/{bookId}")
    public ResponseEntity<Review> getReviewById(@PathVariable Long userId, @PathVariable Long bookId) {
        ReviewPK reviewId = new ReviewPK(userId, bookId);
        Optional<Review> review = reviewRepository.findById(reviewId);
        return review.map(ResponseEntity::ok)
                     .orElseGet(() -> ResponseEntity.notFound().build());
    }

    // Mettre à jour une critique
    @PutMapping("/{userId}/{bookId}")
    public ResponseEntity<Review> updateReview(
            @PathVariable Long userId,
            @PathVariable Long bookId,
            @Valid @RequestBody Review updatedReview) {
        ReviewPK reviewId = new ReviewPK(userId, bookId);
        Optional<Review> existingReview = reviewRepository.findById(reviewId);
        if (existingReview.isPresent()) {
            updatedReview.setReviewId(reviewId);
            Review savedReview = reviewRepository.save(updatedReview);
            return ResponseEntity.ok(savedReview);
        }
        return ResponseEntity.notFound().build();
    }

    // Supprimer une critique
    @DeleteMapping("/{userId}/{bookId}")
    public ResponseEntity<Void> deleteReview(@PathVariable Long userId, @PathVariable Long bookId) {
        ReviewPK reviewId = new ReviewPK(userId, bookId);
        if (reviewRepository.existsById(reviewId)) {
            reviewRepository.deleteById(reviewId);
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.notFound().build();
    }

    // Récupérer toutes les critiques d'un utilisateur
    @GetMapping("/user/{userId}")
    public ResponseEntity<List<Review>> getReviewsByUserId(@PathVariable Long userId) {
        List<Review> reviews = reviewRepository.findByUserId(userId);
        return ResponseEntity.ok(reviews);
    }

    // Récupérer toutes les critiques d'un livre
    @GetMapping("/book/{bookId}")
    public ResponseEntity<List<Review>> getReviewsByBookId(@PathVariable Long bookId) {
        List<Review> reviews = reviewRepository.findByBookId(bookId);
        return ResponseEntity.ok(reviews);
    }
}