package com.dawanproject.booktracker.mappers;

import com.dawanproject.booktracker.dtos.ReviewDto;
import com.dawanproject.booktracker.entities.Review;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

/**
 * Mapper for converting between Review entity and ReviewDTO.
 */
@Mapper(componentModel = "spring")
public interface ReviewMapper {

    /**
     * Converts a Review entity to a ReviewDTO.
     *
     * @param review The Review entity to convert.
     * @return The corresponding ReviewDTO.
     */
    @Mapping(source = "reviewId.userId", target = "userId")
    @Mapping(source = "reviewId.bookId", target = "bookId")
    ReviewDto toDTO(Review review);

    /**
     * Converts a ReviewDTO to a Review entity.
     *
     * @param reviewDTO The ReviewDTO to convert.
     * @return The corresponding Review entity.
     */
    @Mapping(source = "userId", target = "reviewId.userId")
    @Mapping(source = "bookId", target = "reviewId.bookId")
    @Mapping(target = "book", ignore = true)
    @Mapping(target = "user", ignore = true)
    Review toEntity(ReviewDto reviewDTO);
}