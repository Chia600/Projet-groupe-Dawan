package com.dawanproject.booktracker.repositories;

import java.util.Optional;
import com.dawanproject.booktracker.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByUsername(String username);
    Optional<User> findByEmail(String email);
}
