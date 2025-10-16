package com.dawanproject.booktracker.repositories;

import com.dawanproject.booktracker.entities.UserGoogleBookReview;
import com.dawanproject.booktracker.entities.UserGoogleBookReviewPK;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserGoogleBookReviewRepository extends JpaRepository<UserGoogleBookReview, UserGoogleBookReviewPK> {
    List<UserGoogleBookReview> findByIdUserId(Long userId);
    Optional<UserGoogleBookReview> findByIdUserIdAndIdGoogleBookId(Long userId, String googleBookId);
    void deleteByIdUserIdAndIdGoogleBookId(Long userId, String googleBookId);
}