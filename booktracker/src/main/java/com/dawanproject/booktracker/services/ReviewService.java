package com.dawanproject.booktracker.services;

import com.dawanproject.booktracker.dtos.ReviewDto;
import com.dawanproject.booktracker.entities.Review;
import com.dawanproject.booktracker.entities.ReviewPK;
import com.dawanproject.booktracker.mappers.ReviewMapper;
import com.dawanproject.booktracker.repositories.ReviewRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * Service class for managing Review entities.
 */
@Service
public class ReviewService {

    private final ReviewRepository reviewRepository;
    private final ReviewMapper reviewMapper;

    public ReviewService(ReviewRepository reviewRepository, ReviewMapper reviewMapper) {
        this.reviewRepository = reviewRepository;
        this.reviewMapper = reviewMapper;
    }

    /**
     * Creates a new review.
     *
     * @param reviewDTO The review data to create.
     * @return The created ReviewDTO.
     */
    public ReviewDto createReview(ReviewDto reviewDTO) {
        Review review = reviewMapper.toEntity(reviewDTO);
        review.setCreationDate(LocalDate.now());
        Review savedReview = reviewRepository.save(review);
        return reviewMapper.toDTO(savedReview);
    }

    /**
     * Retrieves all reviews.
     *
     * @return List of all ReviewDTOs.
     */
    public List<ReviewDto> getAllReviews() {
        return reviewRepository.findAll().stream()
                .map(reviewMapper::toDTO)
                .collect(Collectors.toList());
    }

    /**
     * Retrieves a review by its composite ID (userId and bookId).
     *
     * @param userId The ID of the user associated with the review.
     * @param bookId The ID of the book associated with the review.
     * @return Optional containing the ReviewDTO if found, empty otherwise.
     */
    public Optional<ReviewDto> getReviewById(Long userId, Long bookId) {
        return reviewRepository.findById(new ReviewPK(userId, bookId))
                .map(reviewMapper::toDTO);
    }

    /**
     * Updates an existing review.
     *
     * @param userId The ID of the user associated with the review.
     * @param bookId The ID of the book associated with the review.
     * @param reviewDTO The updated review data.
     * @return Optional containing the updated ReviewDTO if found, empty otherwise.
     */
    public Optional<ReviewDto> updateReview(Long userId, Long bookId, ReviewDto reviewDTO) {
        Optional<Review> existingReview = reviewRepository.findById(new ReviewPK(userId, bookId));
        if (existingReview.isPresent()) {
            Review review = reviewMapper.toEntity(reviewDTO);
            review.setReviewId(new ReviewPK(userId, bookId));
            review.setCreationDate(existingReview.get().getCreationDate());
            Review savedReview = reviewRepository.save(review);
            return Optional.of(reviewMapper.toDTO(savedReview));
        }
        return Optional.empty();
    }

    /**
     * Deletes a review by its composite ID (userId and bookId).
     *
     * @param userId The ID of the user associated with the review.
     * @param bookId The ID of the book associated with the review.
     * @return True if deleted, false if not found.
     */
    public boolean deleteReview(Long userId, Long bookId) {
        ReviewPK reviewId = new ReviewPK(userId, bookId);
        if (reviewRepository.existsById(reviewId)) {
            reviewRepository.deleteById(reviewId);
            return true;
        }
        return false;
    }

    /**
     * Retrieves all reviews for a specific user.
     *
     * @param userId The ID of the user.
     * @return List of ReviewDTOs for the user.
     */
    public List<ReviewDto> getReviewsByUserId(Long userId) {
        return reviewRepository.findByUserId(userId).stream()
                .map(reviewMapper::toDTO)
                .collect(Collectors.toList());
    }

    /**
     * Retrieves all reviews for a specific book.
     *
     * @param bookId The ID of the book.
     * @return List of ReviewDTOs for the book.
     */
    public List<ReviewDto> getReviewsByBookId(Long bookId) {
        return reviewRepository.findByBookId(bookId).stream()
                .map(reviewMapper::toDTO)
                .collect(Collectors.toList());
    }
}