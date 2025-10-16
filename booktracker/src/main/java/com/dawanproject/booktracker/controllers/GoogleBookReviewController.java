package com.dawanproject.booktracker.controllers;

import com.dawanproject.booktracker.dtos.GoogleBookReviewDto;
import com.dawanproject.booktracker.entities.UserGoogleBookReview;
import com.dawanproject.booktracker.entities.UserGoogleBookReviewPK;
import com.dawanproject.booktracker.repositories.UserGoogleBookReviewRepository;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/google-book-reviews")
@Validated
@RequiredArgsConstructor
public class GoogleBookReviewController {

    private final UserGoogleBookReviewRepository reviewRepository;

    /**
     * Creates a new review for a Google Book.
     */
    @PostMapping
    public ResponseEntity<GoogleBookReviewDto> createReview(@Valid @RequestBody GoogleBookReviewDto dto) {
        UserGoogleBookReview review = new UserGoogleBookReview(
            dto.getUserId(),
            dto.getGoogleBookId(),
            dto.getReview(),
            dto.getRating()
        );
        
        UserGoogleBookReview saved = reviewRepository.save(review);
        
        return ResponseEntity.status(201).body(toDto(saved));
    }

    /**
     * Retrieves a review by userId and googleBookId.
     */
    @GetMapping("/{userId}/{googleBookId}")
    public ResponseEntity<GoogleBookReviewDto> getReview(
            @PathVariable Long userId, 
            @PathVariable String googleBookId) {
        return reviewRepository.findByIdUserIdAndIdGoogleBookId(userId, googleBookId)
                .map(this::toDto)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    /**
     * Updates an existing review.
     */
    @PutMapping("/{userId}/{googleBookId}")
    public ResponseEntity<GoogleBookReviewDto> updateReview(
            @PathVariable Long userId,
            @PathVariable String googleBookId,
            @Valid @RequestBody GoogleBookReviewDto dto) {
        
        UserGoogleBookReviewPK id = new UserGoogleBookReviewPK(userId, googleBookId);
        
        return reviewRepository.findById(id)
                .map(existing -> {
                    existing.setReview(dto.getReview());
                    existing.setRating(dto.getRating());
                    // Garder la date de création originale
                    UserGoogleBookReview updated = reviewRepository.save(existing);
                    return ResponseEntity.ok(toDto(updated));
                })
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    /**
     * Deletes a review.
     */
    @DeleteMapping("/{userId}/{googleBookId}")
    @Transactional
    public ResponseEntity<Void> deleteReview(@PathVariable Long userId, @PathVariable String googleBookId) {
        reviewRepository.deleteByIdUserIdAndIdGoogleBookId(userId, googleBookId);
        return ResponseEntity.noContent().build();
    }

    /**
     * Retrieves all reviews for a specific user.
     */
    @GetMapping("/user/{userId}")
    public ResponseEntity<List<GoogleBookReviewDto>> getUserReviews(@PathVariable Long userId) {
        List<GoogleBookReviewDto> reviews = reviewRepository.findByIdUserId(userId)
                .stream()
                .map(this::toDto)
                .collect(Collectors.toList());
        return ResponseEntity.ok(reviews);
    }

    private GoogleBookReviewDto toDto(UserGoogleBookReview entity) {
        return new GoogleBookReviewDto(
            entity.getId().getUserId(),
            entity.getId().getGoogleBookId(),
            entity.getReview(),
            entity.getRating(),
            entity.getCreationDate()
        );
    }
}