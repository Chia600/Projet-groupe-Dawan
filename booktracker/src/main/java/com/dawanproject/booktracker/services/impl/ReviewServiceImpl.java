package com.dawanproject.booktracker.services.impl;

import com.dawanproject.booktracker.dtos.ReviewDto;
import com.dawanproject.booktracker.entities.Review;
import com.dawanproject.booktracker.entities.ReviewPK;
import com.dawanproject.booktracker.mappers.ReviewMapper;
import com.dawanproject.booktracker.repositories.ReviewRepository;
import com.dawanproject.booktracker.services.ReviewService;

import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * Implementation of ReviewService for managing Review entities.
 */
@Service
public class ReviewServiceImpl implements ReviewService {

    private final ReviewRepository reviewRepository;
    private final ReviewMapper reviewMapper;

    public ReviewServiceImpl(ReviewRepository reviewRepository, ReviewMapper reviewMapper) {
        this.reviewRepository = reviewRepository;
        this.reviewMapper = reviewMapper;
    }

    @Override
    public ReviewDto createReview(ReviewDto reviewDTO) {
        Review review = reviewMapper.toEntity(reviewDTO);
        review.setCreationDate(LocalDate.now());
        Review savedReview = reviewRepository.save(review);
        return reviewMapper.toDTO(savedReview);
    }

    @Override
    public List<ReviewDto> getAllReviews() {
        return reviewRepository.findAll().stream()
                .map(reviewMapper::toDTO)
                .collect(Collectors.toList());
    }

    @Override
    public Optional<ReviewDto> getReviewById(Long userId, Long bookId) {
        return reviewRepository.findById(new ReviewPK(userId, bookId))
                .map(reviewMapper::toDTO);
    }

    @Override
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

    @Override
    public boolean deleteReview(Long userId, Long bookId) {
        ReviewPK reviewId = new ReviewPK(userId, bookId);
        if (reviewRepository.existsById(reviewId)) {
            reviewRepository.deleteById(reviewId);
            return true;
        }
        return false;
    }

    @Override
    public List<ReviewDto> getReviewsByUserId(Long userId) {
        return reviewRepository.findByReviewId_UserId(userId).stream()
                .map(reviewMapper::toDTO)
                .collect(Collectors.toList());
    }

    @Override
    public List<ReviewDto> getReviewsByBookId(Long bookId) {
        return reviewRepository.findByReviewId_BookId(bookId).stream()
                .map(reviewMapper::toDTO)
                .collect(Collectors.toList());
    }
}
