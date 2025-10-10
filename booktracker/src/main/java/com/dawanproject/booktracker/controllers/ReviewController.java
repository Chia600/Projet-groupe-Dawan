package com.dawanproject.booktracker.controllers;

import com.dawanproject.booktracker.dtos.ReviewDto;
import com.dawanproject.booktracker.services.ReviewService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * REST controller for managing Review entities.
 */
@RestController
@RequestMapping("/reviews")
@Validated
@RequiredArgsConstructor
public class ReviewController {

    private final ReviewService reviewService;

    /**
     * Creates a new review.
     *
     * @param reviewDTO The review data to create.
     * @return ResponseEntity containing the created review and HTTP status 201 (Created).
     */
    @PostMapping
    public ResponseEntity<ReviewDto> createReview(@Valid @RequestBody ReviewDto reviewDTO) {
        ReviewDto createdReview = reviewService.createReview(reviewDTO);
        return ResponseEntity.status(201).body(createdReview);
    }

    /**
     * Retrieves all reviews.
     *
     * @return ResponseEntity containing the list of all reviews and HTTP status 200 (OK).
     */
    @GetMapping
    public ResponseEntity<List<ReviewDto>> getAllReviews() {
        return ResponseEntity.ok(reviewService.getAllReviews());
    }

    /**
     * Retrieves a review by its composite ID (userId and bookId).
     *
     * @param userId The ID of the user associated with the review.
     * @param bookId The ID of the book associated with the review.
     * @return ResponseEntity containing the review if found, or HTTP status 404 (Not Found).
     */
    @GetMapping("/{userId}/{bookId}")
    public ResponseEntity<ReviewDto> getReviewById(@PathVariable Long userId, @PathVariable Long bookId) {
        return reviewService.getReviewById(userId, bookId)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    /**
     * Updates an existing review.
     *
     * @param userId    The ID of the user associated with the review.
     * @param bookId    The ID of the book associated with the review.
     * @param reviewDTO The updated review data.
     * @return ResponseEntity containing the updated review if found, or HTTP status 404 (Not Found).
     */
    @PutMapping("/{userId}/{bookId}")
    public ResponseEntity<ReviewDto> updateReview(
            @PathVariable Long userId,
            @PathVariable Long bookId,
            @Valid @RequestBody ReviewDto reviewDTO) {
        return reviewService.updateReview(userId, bookId, reviewDTO)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    /**
     * Deletes a review by its composite ID (userId and bookId).
     *
     * @param userId The ID of the user associated with the review.
     * @param bookId The ID of the book associated with the review.
     * @return ResponseEntity with HTTP status 204 (No Content) if deleted, or 404 (Not Found).
     */
    @DeleteMapping("/{userId}/{bookId}")
    public ResponseEntity<Void> deleteReview(@PathVariable Long userId, @PathVariable Long bookId) {
        if (reviewService.deleteReview(userId, bookId)) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.notFound().build();
    }

    /**
     * Retrieves all reviews for a specific user.
     *
     * @param userId The ID of the user.
     * @return ResponseEntity containing the list of reviews for the user and HTTP status 200 (OK).
     */
    @GetMapping("/user/{userId}")
    public ResponseEntity<List<ReviewDto>> getReviewsByUserId(@PathVariable Long userId) {
        return ResponseEntity.ok(reviewService.getReviewsByUserId(userId));
    }

    /**
     * Retrieves all reviews for a specific book.
     *
     * @param bookId The ID of the book.
     * @return ResponseEntity containing the list of reviews for the book and HTTP status 200 (OK).
     */
    @GetMapping("/book/{bookId}")
    public ResponseEntity<List<ReviewDto>> getReviewsByBookId(@PathVariable Long bookId) {
        return ResponseEntity.ok(reviewService.getReviewsByBookId(bookId));
    }
}