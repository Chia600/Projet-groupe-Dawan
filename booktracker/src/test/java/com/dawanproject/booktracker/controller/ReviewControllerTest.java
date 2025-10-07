package com.dawanproject.booktracker.controller;

import com.dawanproject.booktracker.controllers.ReviewController;
import com.dawanproject.booktracker.entities.Review;
import com.dawanproject.booktracker.entities.ReviewPK;
import com.dawanproject.booktracker.repositories.ReviewRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Unit tests for ReviewController.
 */
@WebMvcTest(ReviewController.class)
class ReviewControllerTest {

    @Autowired
    private MockMvc mockMvc;

    private ReviewRepository reviewRepository;

    @Autowired
    private ObjectMapper objectMapper;

    /**
     * Tests the creation of a new review with authentication.
     *
     * @throws Exception if an error occurs during the test.
     */
    @Test
    @WithMockUser(username = "user", roles = {"USER"})
    void testCreateReview_Success() throws Exception {
        Review review = new Review();
        review.setReviewId(new ReviewPK(1L, 1L));
        review.setReview("Great book!");
        review.setRating(4);
        review.setCreationDate(LocalDate.now());

        when(reviewRepository.save(any(Review.class))).thenReturn(review);

        mockMvc.perform(post("/reviews")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(review)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.reviewId.userId").value(1L))
                .andExpect(jsonPath("$.reviewId.bookId").value(1L))
                .andExpect(jsonPath("$.review").value("Great book!"))
                .andExpect(jsonPath("$.rating").value(4));
    }

    /**
     * Tests retrieving all reviews with authentication.
     *
     * @throws Exception if an error occurs during the test.
     */
    @Test
    @WithMockUser(username = "user", roles = {"USER"})
    void testGetAllReviews_Success() throws Exception {
        Review review1 = new Review();
        review1.setReviewId(new ReviewPK(1L, 1L));
        review1.setReview("Great book!");

        Review review2 = new Review();
        review2.setReviewId(new ReviewPK(2L, 1L));
        review2.setReview("Amazing!");

        when(reviewRepository.findAll()).thenReturn(Arrays.asList(review1, review2));

        mockMvc.perform(get("/reviews"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].reviewId.userId").value(1L))
                .andExpect(jsonPath("$[0].review").value("Great book!"))
                .andExpect(jsonPath("$[1].reviewId.userId").value(2L))
                .andExpect(jsonPath("$[1].review").value("Amazing!"));
    }

    /**
     * Tests retrieving a review by its composite ID with authentication.
     *
     * @throws Exception if an error occurs during the test.
     */
    @Test
    @WithMockUser(username = "user", roles = {"USER"})
    void testGetReviewById_Success() throws Exception {
        Review review = new Review();
        review.setReviewId(new ReviewPK(1L, 1L));
        review.setReview("Great book!");

        when(reviewRepository.findById(new ReviewPK(1L, 1L))).thenReturn(Optional.of(review));

        mockMvc.perform(get("/reviews/1/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.reviewId.userId").value(1L))
                .andExpect(jsonPath("$.reviewId.bookId").value(1L))
                .andExpect(jsonPath("$.review").value("Great book!"));
    }

    /**
     * Tests retrieving a review by its composite ID when the review does not exist.
     *
     * @throws Exception if an error occurs during the test.
     */
    @Test
    @WithMockUser(username = "user", roles = {"USER"})
    void testGetReviewById_NotFound() throws Exception {
        when(reviewRepository.findById(new ReviewPK(1L, 1L))).thenReturn(Optional.empty());

        mockMvc.perform(get("/reviews/1/1"))
                .andExpect(status().isNotFound());
    }

    /**
     * Tests updating an existing review with authentication.
     *
     * @throws Exception if an error occurs during the test.
     */
    @Test
    @WithMockUser(username = "user", roles = {"USER"})
    void testUpdateReview_Success() throws Exception {
        Review existingReview = new Review();
        existingReview.setReviewId(new ReviewPK(1L, 1L));
        existingReview.setReview("Old review");

        Review updatedReview = new Review();
        updatedReview.setReviewId(new ReviewPK(1L, 1L));
        updatedReview.setReview("Updated review");
        updatedReview.setRating(5);

        when(reviewRepository.findById(new ReviewPK(1L, 1L))).thenReturn(Optional.of(existingReview));
        when(reviewRepository.save(any(Review.class))).thenReturn(updatedReview);

        mockMvc.perform(put("/reviews/1/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(updatedReview)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.reviewId.userId").value(1L))
                .andExpect(jsonPath("$.reviewId.bookId").value(1L))
                .andExpect(jsonPath("$.review").value("Updated review"))
                .andExpect(jsonPath("$.rating").value(5));
    }

    /**
     * Tests updating a review that does not exist.
     *
     * @throws Exception if an error occurs during the test.
     */
    @Test
    @WithMockUser(username = "user", roles = {"USER"})
    void testUpdateReview_NotFound() throws Exception {
        Review updatedReview = new Review();
        updatedReview.setReview("Updated review");

        when(reviewRepository.findById(new ReviewPK(1L, 1L))).thenReturn(Optional.empty());

        mockMvc.perform(put("/reviews/1/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(updatedReview)))
                .andExpect(status().isNotFound());
    }

    /**
     * Tests deleting an existing review with authentication.
     *
     * @throws Exception if an error occurs during the test.
     */
    @Test
    @WithMockUser(username = "user", roles = {"USER"})
    void testDeleteReview_Success() throws Exception {
        when(reviewRepository.existsById(new ReviewPK(1L, 1L))).thenReturn(true);

        mockMvc.perform(delete("/reviews/1/1"))
                .andExpect(status().isNoContent());
    }

    /**
     * Tests deleting a review that does not exist.
     *
     * @throws Exception if an error occurs during the test.
     */
    @Test
    @WithMockUser(username = "user", roles = {"USER"})
    void testDeleteReview_NotFound() throws Exception {
        when(reviewRepository.existsById(new ReviewPK(1L, 1L))).thenReturn(false);

        mockMvc.perform(delete("/reviews/1/1"))
                .andExpect(status().isNotFound());
    }

    /**
     * Tests retrieving all reviews for a specific user with authentication.
     *
     * @throws Exception if an error occurs during the test.
     */
    @Test
    @WithMockUser(username = "user", roles = {"USER"})
    void testGetReviewsByUserId_Success() throws Exception {
        Review review = new Review();
        review.setReviewId(new ReviewPK(1L, 1L));
        review.setReview("Great book!");

        when(reviewRepository.findByUserId(1L)).thenReturn(Arrays.asList(review));

        mockMvc.perform(get("/reviews/user/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].reviewId.userId").value(1L))
                .andExpect(jsonPath("$[0].review").value("Great book!"));
    }

    /**
     * Tests retrieving all reviews for a specific book with authentication.
     *
     * @throws Exception if an error occurs during the test.
     */
    @Test
    @WithMockUser(username = "user", roles = {"USER"})
    void testGetReviewsByBookId_Success() throws Exception {
        Review review = new Review();
        review.setReviewId(new ReviewPK(1L, 1L));
        review.setReview("Great book!");

        when(reviewRepository.findByBookId(1L)).thenReturn(Arrays.asList(review));

        mockMvc.perform(get("/reviews/book/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].reviewId.bookId").value(1L))
                .andExpect(jsonPath("$[0].review").value("Great book!"));
    }
}