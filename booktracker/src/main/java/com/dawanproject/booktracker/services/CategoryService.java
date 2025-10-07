package com.dawanproject.booktracker.services;

import com.dawanproject.booktracker.entities.Book;
import com.dawanproject.booktracker.entities.Category;

import java.util.List;
import java.util.Optional;

public interface CategoryService {

    // Créer une nouvelle catégorie (Category sans livres)
    Category createCategory(Category category);

    // Récupérer toutes les catégories (sans livres)
    List<Category> getAllCategories();

    // Récupérer une catégorie par ID (sans les livres pour éviter boucle JSON)
    Optional<Category> getCategoryById(Long id);

    // Mettre à jour une catégorie (seulement le genre, pas les livres)
    Optional<Category> updateCategory(Long id, Category updatedCategory);

    // Supprimer une catégorie
    boolean deleteCategory(Long id);

    // Récupérer une catégorie par genre (insensible à la casse, sans livres)
    Optional<Category> getCategoryByGenre(String genre);

    // Récupérer tous les livres d'une catégorie
    List<Book> getBooksByCategory(Long id);
}
