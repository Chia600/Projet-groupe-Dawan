package com.dawanproject.booktracker.repositories;

import com.dawanproject.booktracker.entities.Author;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface AuthorRepository extends JpaRepository<Author, Long> {

    /**
     * Récupère une liste d'auteur dont le nom Like name
     *
     * @param name critère de recherche (peut-être partiel)
     * @return List<Author>
     */
    List<Author> findByLastnameLike(String name);
}
