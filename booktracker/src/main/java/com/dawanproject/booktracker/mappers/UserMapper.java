package com.dawanproject.booktracker.mappers;

import com.dawanproject.booktracker.dtos.UserDto;
import com.dawanproject.booktracker.entities.Book;
import com.dawanproject.booktracker.entities.Review;
import com.dawanproject.booktracker.entities.User;
import org.mapstruct.*;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * Mapper for converting between User entity and UserDTO.
 */
@Mapper(componentModel = MappingConstants.ComponentModel.SPRING, unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface UserMapper {
    /**
     * Maps a set of Review entities to a list of review IDs (bookIds).
     *
     * @param reviews The set of Review entities.
     * @return A list of book IDs.
     */
    @Named("mapReviewsToIds")
    default List<Long> mapReviewsToIds(Set<Review> reviews) {
        if (reviews == null) {
            return null;
        }
        return reviews.stream()
                .map(review -> review.getReviewId().getBookId())
                .collect(Collectors.toList());
    }

    @Named("mapBooksToIds")
    default List<Long> mapBooksToIds(Set<Book> books) {
        if (books == null) {
            return null;
        }
        return books.stream()
                .map(b -> b.getBookId())
                .collect(Collectors.toList());
    }

    /**
     * Converts a User entity to a UserDTO.
     *
     * @param user The User entity to convert.
     * @return The corresponding UserDTO.
     */
    @Mapping(source = "reviews", target = "reviewIds", qualifiedByName = "mapReviewsToIds")
    @Mapping(source = "books", target = "bookIds", qualifiedByName = "mapBooksToIds")
    @Mapping(target = "password", ignore = true)
    UserDto toDTO(User user);

    /**
     * Converts a UserDTO to a User entity.
     *
     * @param userDTO The UserDTO to convert.
     * @return The corresponding User entity.
     */

    User toEntity(UserDto userDTO);

}