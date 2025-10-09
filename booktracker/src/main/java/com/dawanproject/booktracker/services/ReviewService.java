package com.dawanproject.booktracker.services;

import com.dawanproject.booktracker.dtos.ReviewDto;

import java.util.List;
import java.util.Optional;

/**
 * Service interface for managing Review entities.
 */
public interface ReviewService {

    /**
     * Creates a new review.
     *
     * @param reviewDTO The review data to create.
     * @return The created ReviewDTO.
     */
    ReviewDto createReview(ReviewDto reviewDTO);

    /**
     * Retrieves all reviews.
     *
     * @return List of all ReviewDTOs.
     */
    List<ReviewDto> getAllReviews();

    /**
     * Retrieves a review by its composite ID (userId and bookId).
     *
     * @param userId The ID of the user associated with the review.
     * @param bookId The ID of the book associated with the review.
     * @return Optional containing the ReviewDTO if found, empty otherwise.
     */
    Optional<ReviewDto> getReviewById(Long userId, Long bookId);

    /**
     * Updates an existing review.
     *
     * @param userId The ID of the user associated with the review.
     * @param bookId The ID of the book associated with the review.
     * @param reviewDTO The updated review data.
     * @return Optional containing the updated ReviewDTO if found, empty otherwise.
     */
    Optional<ReviewDto> updateReview(Long userId, Long bookId, ReviewDto reviewDTO);

    /**
     * Deletes a review by its composite ID (userId and bookId).
     *
     * @param userId The ID of the user associated with the review.
     * @param bookId The ID of the book associated with the review.
     * @return True if deleted, false if not found.
     */
    boolean deleteReview(Long userId, Long bookId);

    /**
     * Retrieves all reviews for a specific user.
     *
     * @param userId The ID of the user.
     * @return List of ReviewDTOs for the user.
     */
    List<ReviewDto> getReviewsByUserId(Long userId);

    /**
     * Retrieves all reviews for a specific book.
     *
     * @param bookId The ID of the book.
     * @return List of ReviewDTOs for the book.
     */
    List<ReviewDto> getReviewsByBookId(Long bookId);
}