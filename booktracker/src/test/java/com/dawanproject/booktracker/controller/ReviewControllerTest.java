package com.dawanproject.booktracker.controller;

import com.dawanproject.booktracker.controllers.ReviewController;
import com.dawanproject.booktracker.dtos.ReviewDto;
import com.dawanproject.booktracker.security.SecurityConfig;
import com.dawanproject.booktracker.services.ReviewService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * Unit tests for ReviewController.
 */
@WebMvcTest(ReviewController.class)
@AutoConfigureMockMvc(addFilters = false)
@Import(SecurityConfig.class)
class ReviewControllerTest {

    @Autowired
    private MockMvc mockMvc;
    @MockitoBean
    private ReviewService reviewService;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private WebApplicationContext context;

    @BeforeEach
    void setup() {
        mockMvc = MockMvcBuilders
                .webAppContextSetup(context)
                .apply(SecurityMockMvcConfigurers.springSecurity())
                .build();
    }

    @Test
    @WithMockUser(username = "user", roles = {"USER"})
    void testCreateReview_Success() throws Exception {
        ReviewDto reviewDTO = new ReviewDto(1L, 1L, "Great book!", 4, null);
        ReviewDto responseDTO = new ReviewDto(1L, 1L, "Great book!", 4, LocalDate.now());

        when(reviewService.createReview(any(ReviewDto.class))).thenReturn(responseDTO);

        mockMvc.perform(post("/reviews")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(reviewDTO)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.userId").value(1L))
                .andExpect(jsonPath("$.bookId").value(1L))
                .andExpect(jsonPath("$.review").value("Great book!"))
                .andExpect(jsonPath("$.rating").value(4));
    }

    @Test
    @WithMockUser(username = "user", roles = {"USER"})
    void testGetAllReviews_Success() throws Exception {
        ReviewDto reviewDTO1 = new ReviewDto(1L, 1L, "Great book!", 4, LocalDate.now());
        ReviewDto reviewDTO2 = new ReviewDto(2L, 1L, "Amazing!", 5, LocalDate.now());

        when(reviewService.getAllReviews()).thenReturn(Arrays.asList(reviewDTO1, reviewDTO2));

        mockMvc.perform(get("/reviews"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].userId").value(1L))
                .andExpect(jsonPath("$[0].review").value("Great book!"))
                .andExpect(jsonPath("$[1].userId").value(2L))
                .andExpect(jsonPath("$[1].review").value("Amazing!"));
    }

    @Test
    @WithMockUser(username = "user", roles = {"USER"})
    void testGetReviewById_Success() throws Exception {
        ReviewDto reviewDTO = new ReviewDto(1L, 1L, "Great book!", 4, LocalDate.now());

        when(reviewService.getReviewById(1L, 1L)).thenReturn(Optional.of(reviewDTO));

        mockMvc.perform(get("/reviews/1/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.userId").value(1L))
                .andExpect(jsonPath("$.bookId").value(1L))
                .andExpect(jsonPath("$.review").value("Great book!"));
    }

    @Test
    @WithMockUser(username = "user", roles = {"USER"})
    void testGetReviewById_NotFound() throws Exception {
        when(reviewService.getReviewById(1L, 1L)).thenReturn(Optional.empty());

        mockMvc.perform(get("/reviews/1/1"))
                .andExpect(status().isNotFound());
    }

    @Test
    @WithMockUser(username = "user", roles = {"USER"})
    void testUpdateReview_Success() throws Exception {
        ReviewDto reviewDTO = new ReviewDto(1L, 1L, "Updated review", 5, null);
        ReviewDto responseDTO = new ReviewDto(1L, 1L, "Updated review", 5, LocalDate.now());

        when(reviewService.updateReview(1L, 1L, any(ReviewDto.class))).thenReturn(Optional.of(responseDTO));

        mockMvc.perform(put("/reviews/1/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(reviewDTO)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.userId").value(1L))
                .andExpect(jsonPath("$.bookId").value(1L))
                .andExpect(jsonPath("$.review").value("Updated review"))
                .andExpect(jsonPath("$.rating").value(5));
    }

    @Test
    @WithMockUser(username = "user", roles = {"USER"})
    void testUpdateReview_NotFound() throws Exception {
        ReviewDto reviewDTO = new ReviewDto(1L, 1L, "Updated review", 5, null);

        when(reviewService.updateReview(1L, 1L, any(ReviewDto.class))).thenReturn(Optional.empty());

        mockMvc.perform(put("/reviews/1/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(reviewDTO)))
                .andExpect(status().isNotFound());
    }

    @Test
    @WithMockUser(username = "user", roles = {"USER"})
    void testDeleteReview_Success() throws Exception {
        when(reviewService.deleteReview(1L, 1L)).thenReturn(true);

        mockMvc.perform(delete("/reviews/1/1"))
                .andExpect(status().isNoContent());
    }

    @Test
    @WithMockUser(username = "user", roles = {"USER"})
    void testDeleteReview_NotFound() throws Exception {
        when(reviewService.deleteReview(1L, 1L)).thenReturn(false);

        mockMvc.perform(delete("/reviews/1/1"))
                .andExpect(status().isNotFound());
    }

    @Test
    @WithMockUser(username = "user", roles = {"USER"})
    void testGetReviewsByUserId_Success() throws Exception {
        ReviewDto reviewDTO = new ReviewDto(1L, 1L, "Great book!", 4, LocalDate.now());

        when(reviewService.getReviewsByUserId(1L)).thenReturn(Arrays.asList(reviewDTO));

        mockMvc.perform(get("/reviews/user/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].userId").value(1L))
                .andExpect(jsonPath("$[0].review").value("Great book!"));
    }

    @Test
    @WithMockUser(username = "user", roles = {"USER"})
    void testGetReviewsByBookId_Success() throws Exception {
        ReviewDto reviewDTO = new ReviewDto(1L, 1L, "Great book!", 4, LocalDate.now());

        when(reviewService.getReviewsByBookId(1L)).thenReturn(Arrays.asList(reviewDTO));

        mockMvc.perform(get("/reviews/book/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].bookId").value(1L))
                .andExpect(jsonPath("$[0].review").value("Great book!"));
    }
}