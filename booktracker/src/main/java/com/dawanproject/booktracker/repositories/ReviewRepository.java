package com.dawanproject.booktracker.repositories;

import com.dawanproject.booktracker.entities.Review;
import com.dawanproject.booktracker.entities.ReviewPK;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ReviewRepository extends JpaRepository<Review, ReviewPK> {
    List<Review> findByReviewId_BookId(long bookId);

    List<Review> findByReviewId_UserId(long userId);
}
