package com.dawanproject.booktracker.mappers;

import com.dawanproject.booktracker.dtos.UserDto;
import com.dawanproject.booktracker.entities.Review;
import com.dawanproject.booktracker.entities.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Mapper for converting between User entity and UserDTO.
 */
@Mapper(componentModel = "spring")
public interface UserMapper {
     /**
     * Maps a set of Review entities to a list of review IDs (bookIds).
     *
     * @param reviews The set of Review entities.
     * @return A list of book IDs.
     */
    @Named("mapReviewsToIds")
    default List<Long> mapReviewsToIds(java.util.Set<Review> reviews) {
        if (reviews == null) {
            return null;
        }
        return reviews.stream()
                .map(review -> review.getReviewId().getBookId())
                .collect(Collectors.toList());
    }
    /**
     * Converts a User entity to a UserDTO.
     *
     * @param user The User entity to convert.
     * @return The corresponding UserDTO.
     */
    @Mapping(source = "reviews", target = "reviewIds", qualifiedByName = "mapReviewsToIds")
    @Mapping(target = "password", ignore = true)
    UserDto toDTO(User user);

    /**
     * Converts a UserDTO to a User entity.
     *
     * @param userDTO The UserDTO to convert.
     * @return The corresponding User entity.
     */
    @Mapping(target = "userId", ignore = true)
    @Mapping(target = "reviews", ignore = true)
    @Mapping(target = "firstname", ignore = true)
    @Mapping(target = "lastname", ignore = true)
    @Mapping(target = "version", ignore = true)
    @Mapping(target = "books", ignore = true)
    @Mapping(target = "picture", ignore = true)
    @Mapping(target = "subscriptionDate", ignore = true)
    User toEntity(UserDto userDTO);
   
}