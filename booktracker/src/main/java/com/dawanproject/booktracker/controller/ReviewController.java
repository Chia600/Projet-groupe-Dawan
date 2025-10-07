package com.dawanproject.booktracker.controller;

import com.dawanproject.booktracker.entities.Review;
import com.dawanproject.booktracker.entities.ReviewPK;
import com.dawanproject.booktracker.repositories.ReviewRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;
import java.util.List;
import java.util.Optional;

/**
 * REST controller for managing Review entities.
 */
@RestController
@RequestMapping("/reviews")
@Validated
@RequiredArgsConstructor
public class ReviewController {

    private final ReviewRepository reviewRepository;

    /**
     * Creates a new review.
     *
     * @param review The review to create, provided in the request body.
     * @return ResponseEntity containing the created review and HTTP status 201 (Created).
     * @throws jakarta.validation.ConstraintViolationException if the review data violates validation constraints.
     */
    @PostMapping
    public ResponseEntity<Review> createReview(@Valid @RequestBody Review review) {
        Review savedReview = reviewRepository.save(review);
        return ResponseEntity.status(201).body(savedReview);
    }

    /**
     * Retrieves all reviews.
     *
     * @return ResponseEntity containing the list of all reviews and HTTP status 200 (OK).
     */
    @GetMapping
    public ResponseEntity<List<Review>> getAllReviews() {
        List<Review> reviews = reviewRepository.findAll();
        return ResponseEntity.ok(reviews);
    }

    /**
     * Retrieves a review by its composite ID (userId and bookId).
     *
     * @param userId The ID of the user associated with the review.
     * @param bookId The ID of the book associated with the review.
     * @return ResponseEntity containing the review if found, or HTTP status 404 (Not Found) if not found.
     */
    @GetMapping("/{userId}/{bookId}")
    public ResponseEntity<Review> getReviewById(@PathVariable Long userId, @PathVariable Long bookId) {
        ReviewPK reviewId = new ReviewPK(userId, bookId);
        Optional<Review> review = reviewRepository.findById(reviewId);
        return review.map(ResponseEntity::ok)
                     .orElseGet(() -> ResponseEntity.notFound().build());
    }

    /**
     * Updates an existing review.
     *
     * @param userId The ID of the user associated with the review.
     * @param bookId The ID of the book associated with the review.
     * @param updatedReview The updated review data provided in the request body.
     * @return ResponseEntity containing the updated review if found, or HTTP status 404 (Not Found) if not found.
     * @throws jakarta.validation.ConstraintViolationException if the updated review data violates validation constraints.
     */
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

    /**
     * Deletes a review by its composite ID (userId and bookId).
     *
     * @param userId The ID of the user associated with the review.
     * @param bookId The ID of the book associated with the review.
     * @return ResponseEntity with HTTP status 204 (No Content) if deleted, or 404 (Not Found) if not found.
     */
    @DeleteMapping("/{userId}/{bookId}")
    public ResponseEntity<Void> deleteReview(@PathVariable Long userId, @PathVariable Long bookId) {
        ReviewPK reviewId = new ReviewPK(userId, bookId);
        if (reviewRepository.existsById(reviewId)) {
            reviewRepository.deleteById(reviewId);
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.notFound().build();
    }

    /**
     * Retrieves all reviews for a specific user.
     *
     * @param userId The ID of the user whose reviews are to be retrieved.
     * @return ResponseEntity containing the list of reviews for the user and HTTP status 200 (OK).
     */
    @GetMapping("/user/{userId}")
    public ResponseEntity<List<Review>> getReviewsByUserId(@PathVariable Long userId) {
        List<Review> reviews = reviewRepository.findByUserId(userId);
        return ResponseEntity.ok(reviews);
    }

    /**
     * Retrieves all reviews for a specific book.
     *
     * @param bookId The ID of the book whose reviews are to be retrieved.
     * @return ResponseEntity containing the list of reviews for the book and HTTP status 200 (OK).
     */
    @GetMapping("/book/{bookId}")
    public ResponseEntity<List<Review>> getReviewsByBookId(@PathVariable Long bookId) {
        List<Review> reviews = reviewRepository.findByBookId(bookId);
        return ResponseEntity.ok(reviews);
    }
}