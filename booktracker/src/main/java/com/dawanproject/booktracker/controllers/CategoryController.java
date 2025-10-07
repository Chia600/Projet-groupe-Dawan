package com.dawanproject.booktracker.controllers;

import com.dawanproject.booktracker.entities.Book;
import com.dawanproject.booktracker.entities.Category;
import com.dawanproject.booktracker.repositories.BookRepository;
import com.dawanproject.booktracker.repositories.CategoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/categories")
@Validated
public class CategoryController {

    @Autowired
    private CategoryRepository categoryRepository;

    @Autowired
    private BookRepository bookRepository;

    // Créer une nouvelle catégorie (Category sans livres)
    @PostMapping
    public ResponseEntity<Category> createCategory(@Valid @RequestBody Category category) {
        Category savedCategory = categoryRepository.save(category);
        return ResponseEntity.status(201).body(savedCategory);
    }

    // Récupérer toutes les catégories (sans livres)
    @GetMapping
    public ResponseEntity<List<Category>> getAllCategories() {
        List<Category> categories = categoryRepository.findAll();
        return ResponseEntity.ok(categories);
    }

    // Récupérer une catégorie par ID (sans les livres pour éviter boucle JSON)
    @GetMapping("/{id}")
    public ResponseEntity<Category> getCategoryById(@PathVariable Long id) {
        Optional<Category> category = categoryRepository.findById(id);
        return category.map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    // Mettre à jour une catégorie (seulement le genre, pas les livres)
    @PutMapping("/{id}")
    public ResponseEntity<Category> updateCategory(@PathVariable Long id, @Valid @RequestBody Category updatedCategory) {
        Optional<Category> existingCategory = categoryRepository.findById(id);
        if (existingCategory.isPresent()) {
            Category category = existingCategory.get();
            category.setGenre(updatedCategory.getGenre());
            Category savedCategory = categoryRepository.save(category);
            return ResponseEntity.ok(savedCategory);
        }
        return ResponseEntity.notFound().build();
    }

    // Supprimer une catégorie
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteCategory(@PathVariable Long id) {
        if (categoryRepository.existsById(id)) {
            categoryRepository.deleteById(id);
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.notFound().build();
    }

    // Récupérer une catégorie par genre (insensible à la casse, sans livres)
    @GetMapping("/genre/{genre}")
    public ResponseEntity<Category> getCategoryByGenre(@PathVariable String genre) {
        Optional<Category> category = categoryRepository.findByGenreIgnoreCase(genre);
        return category.map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    // Récupérer tous les livres d'une catégorie
    @GetMapping("/{id}/books")
    public ResponseEntity<List<Book>> getBooksByCategory(@PathVariable Long id) {
        Optional<Category> category = categoryRepository.findById(id);
        if (category.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        List<Book> books = bookRepository.findByCategory_CategoryId(id);
        return ResponseEntity.ok(books);
    }
}
