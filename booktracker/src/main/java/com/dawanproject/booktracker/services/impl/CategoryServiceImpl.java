package com.dawanproject.booktracker.services.impl;

import com.dawanproject.booktracker.entities.Book;
import com.dawanproject.booktracker.entities.Category;
import com.dawanproject.booktracker.repositories.BookRepository;
import com.dawanproject.booktracker.repositories.CategoryRepository;
import com.dawanproject.booktracker.services.CategoryService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

/**
 * Service de gestion des catégories
 */
@Service
public class CategoryServiceImpl implements CategoryService {

    private final CategoryRepository categoryRepository;
    private final BookRepository bookRepository;

    public CategoryServiceImpl(CategoryRepository categoryRepository, BookRepository bookRepository) {
        this.categoryRepository = categoryRepository;
        this.bookRepository = bookRepository;
    }

    // Créer une nouvelle catégorie (Category sans livres)
    @Override
    public Category createCategory(Category category) {
        return categoryRepository.save(category);
    }

    // Récupérer toutes les catégories (sans livres)
    @Override
    public List<Category> getAllCategories() {
        return categoryRepository.findAll();
    }

    // Récupérer une catégorie par ID (sans les livres pour éviter boucle JSON)
    @Override
    public Optional<Category> getCategoryById(Long id) {
        return categoryRepository.findById(id);
    }

    // Mettre à jour une catégorie (seulement le genre, pas les livres)
    @Override
    public Optional<Category> updateCategory(Long id, Category updatedCategory) {
        return categoryRepository.findById(id).map(existing -> {
            existing.setGenre(updatedCategory.getGenre());
            return categoryRepository.save(existing);
        });
    }

    // Supprimer une catégorie
    @Override
    public boolean deleteCategory(Long id) {
        if (categoryRepository.existsById(id)) {
            categoryRepository.deleteById(id);
            return true;
        }
        return false;
    }

    // Récupérer une catégorie par genre (insensible à la casse, sans livres)
    @Override
    public Optional<Category> getCategoryByGenre(String genre) {
        return categoryRepository.findByGenreIgnoreCase(genre);
    }

    // Récupérer tous les livres d'une catégorie
    @Override
    public List<Book> getBooksByCategory(Long id) {
        return bookRepository.findByCategory_CategoryId(id);
    }
}
