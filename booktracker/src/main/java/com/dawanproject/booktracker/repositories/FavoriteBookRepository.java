package com.dawanproject.booktracker.repositories;

import com.dawanproject.booktracker.entities.FavoriteBook;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface FavoriteBookRepository extends JpaRepository<FavoriteBook, Long> {
    List<FavoriteBook> findByUserId(Long userId);
    Optional<FavoriteBook> findByUserIdAndGoogleBookId(Long userId, String googleBookId);
    void deleteByUserIdAndGoogleBookId(Long userId, String googleBookId);
}