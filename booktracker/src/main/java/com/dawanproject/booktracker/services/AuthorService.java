package com.dawanproject.booktracker.services;

import com.dawanproject.booktracker.dtos.AuthorDto;

import java.util.List;
import java.util.Optional;

public interface AuthorService {

    /**
     * Récupère tous les auteurs
     *
     * @return List<AuthorDto>
     */
    List<AuthorDto> getAll();

    /**
     * Récupère un auteur par son id
     *
     * @param id id de l'auteur
     * @return Optional<AuthorDto>
     */
    Optional<AuthorDto> getAuthorById(long id);

    /**
     * Récupère une liste d'auteurs homonymes
     *
     * @param name nom recherché
     * @return Optional<List < AuthorDto>>
     */
    Optional<List<AuthorDto>> getAuthorByName(String name);

    /**
     * Supprime un auteur par son id
     *
     * @param id id de l'auteur
     * @return true si supprimé, sinon false
     */
    boolean deleteById(long id);

    /**
     * Enregistre un auteur si il n'existe pas en base de données
     * Met à jour un auteur déjà en base de données
     *
     * @param dto l'auteur à enregistré / mettre à jour
     * @return l'auteur enregistré / mis à jour
     */
    AuthorDto saveOrUpdate(AuthorDto dto);
}
