package com.dawanproject.booktracker.repositories;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import com.dawanproject.booktracker.entities.Review;
import com.dawanproject.booktracker.entities.ReviewPK;

public interface ReviewRepository extends JpaRepository<Review, ReviewPK> {
    List<Review> findByBookId(long bookId);
    List<Review> findByUserId(long userId);
}
