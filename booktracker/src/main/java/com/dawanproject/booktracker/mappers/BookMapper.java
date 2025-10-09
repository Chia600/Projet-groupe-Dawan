package com.dawanproject.booktracker.mappers;

import com.dawanproject.booktracker.dtos.BookDto;
import com.dawanproject.booktracker.entities.Author;
import com.dawanproject.booktracker.entities.Book;
import com.dawanproject.booktracker.entities.Review;
import org.mapstruct.*;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;


@Mapper(componentModel = MappingConstants.ComponentModel.SPRING, unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface BookMapper {
    @Mapping(source = "bookId", target = "id")
    @Mapping(source = "category.genre", target = "category")
    @Mapping(source = "reviews", target = "reviewIds", qualifiedByName = "mapReviewsToIds")
    @Mapping(source = "author", target = "author", qualifiedByName = "mapAuthortoString")
    BookDto toDto(Book book);

    @Mapping(source = "id", target = "bookId")
    @Mapping(target = "category.genre", source = "category")
    @Mapping(source = "author", target = "author", qualifiedByName = "mapStringtoAuthor")
    Book toEntity(BookDto dto);

    @Named("mapReviewsToIds")
    default List<Long> mapReviewsToIds(Set<Review> reviews) {
        if (reviews == null) {
            return null;
        }
        return reviews.stream()
                .map(review -> review.getReviewId().getBookId())
                .collect(Collectors.toList());
    }

    @Named("mapAuthortoString")
    default String mapAuthortoString(Author author) {
        return author.getFirstname() + " " + author.getLastname();
    }

    @Named("mapStringtoAuthor")
    default Author mapStringtoAuthor(String strAuthor) {
        String[] name = strAuthor.split(" ");
        Author author = new Author();
        author.setFirstname(name[0]);
        author.setLastname(name[1]);
        return author;
    }
}
