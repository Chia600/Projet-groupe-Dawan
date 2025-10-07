package com.dawanproject.booktracker.controllers;

import com.dawanproject.booktracker.dtos.ReviewDto;
import com.dawanproject.booktracker.entities.Review;
import com.dawanproject.booktracker.entities.ReviewPK;
import com.dawanproject.booktracker.mappers.ReviewMapper;
import com.dawanproject.booktracker.repositories.ReviewRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * REST controller for managing Review entities.
 */
@RestController
@RequestMapping("/reviews")
@Validated
@RequiredArgsConstructor
public class ReviewController {

    private final ReviewRepository reviewRepository;
    private final ReviewMapper reviewMapper;

    /**
     * Creates a new review.
     *
     * @param reviewDTO The review data to create, provided in the request body.
     * @return ResponseEntity containing the created review and HTTP status 201 (Created).
     * @throws jakarta.validation.ConstraintViolationException if the review data violates validation constraints.
     */
    @PostMapping
    public ResponseEntity<ReviewDto> createReview(@Valid @RequestBody ReviewDto reviewDTO) {
        Review review = reviewMapper.toEntity(reviewDTO);
        review.setCreationDate(LocalDate.now());
        Review savedReview = reviewRepository.save(review);
        return ResponseEntity.status(201).body(reviewMapper.toDTO(savedReview));
    }

    /**
     * Retrieves all reviews.
     *
     * @return ResponseEntity containing the list of all reviews and HTTP status 200 (OK).
     */
    @GetMapping
    public ResponseEntity<List<ReviewDto>> getAllReviews() {
        List<ReviewDto> reviews = reviewRepository.findAll().stream()
                .map(reviewMapper::toDTO)
                .collect(Collectors.toList());
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
    public ResponseEntity<ReviewDto> getReviewById(@PathVariable Long userId, @PathVariable Long bookId) {
        Optional<Review> review = reviewRepository.findById(new ReviewPK(userId, bookId));
        return review.map(reviewMapper::toDTO)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    /**
     * Updates an existing review.
     *
     * @param userId The ID of the user associated with the review.
     * @param bookId The ID of the book associated with the review.
     * @param reviewDTO The updated review data provided in the request body.
     * @return ResponseEntity containing the updated review if found, or HTTP status 404 (Not Found) if not found.
     * @throws jakarta.validation.ConstraintViolationException if the updated review data violates validation constraints.
     */
    @PutMapping("/{userId}/{bookId}")
    public ResponseEntity<ReviewDto> updateReview(
            @PathVariable Long userId,
            @PathVariable Long bookId,
            @Valid @RequestBody ReviewDto reviewDTO) {
        Optional<Review> existingReview = reviewRepository.findById(new ReviewPK(userId, bookId));
        if (existingReview.isPresent()) {
            Review review = reviewMapper.toEntity(reviewDTO);
            review.setReviewId(new ReviewPK(userId, bookId));
            review.setCreationDate(existingReview.get().getCreationDate());
            Review savedReview = reviewRepository.save(review);
            return ResponseEntity.ok(reviewMapper.toDTO(savedReview));
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
    public ResponseEntity<List<ReviewDto>> getReviewsByUserId(@PathVariable Long userId) {
        List<ReviewDto> reviews = reviewRepository.findByUserId(userId).stream()
                .map(reviewMapper::toDTO)
                .collect(Collectors.toList());
        return ResponseEntity.ok(reviews);
    }

    /**
     * Retrieves all reviews for a specific book.
     *
     * @param bookId The ID of the book whose reviews are to be retrieved.
     * @return ResponseEntity containing the list of reviews for the book and HTTP status 200 (OK).
     */
    @GetMapping("/book/{bookId}")
    public ResponseEntity<List<ReviewDto>> getReviewsByBookId(@PathVariable Long bookId) {
        List<ReviewDto> reviews = reviewRepository.findByBookId(bookId).stream()
                .map(reviewMapper::toDTO)
                .collect(Collectors.toList());
        return ResponseEntity.ok(reviews);
    }
}