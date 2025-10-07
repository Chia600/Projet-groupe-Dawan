package com.dawanproject.booktracker.controllers;

import com.dawanproject.booktracker.entities.Book;
import com.dawanproject.booktracker.entities.Category;
import com.dawanproject.booktracker.services.CategoryService;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/categories")
@Validated
public class CategoryController {

    private final CategoryService categoryService;

    public CategoryController(CategoryService categoryService) {
        this.categoryService = categoryService;
    }

    // Créer une nouvelle catégorie (Category sans livres)
    @PostMapping
    public ResponseEntity<Category> createCategory(@Valid @RequestBody Category category) {
        Category savedCategory = categoryService.createCategory(category);
        return ResponseEntity.status(201).body(savedCategory);
    }

    // Récupérer toutes les catégories (sans livres)
    @GetMapping
    public ResponseEntity<List<Category>> getAllCategories() {
        List<Category> categories = categoryService.getAllCategories();
        return ResponseEntity.ok(categories);
    }

    // Récupérer une catégorie par ID (sans les livres pour éviter boucle JSON)
    @GetMapping("/{id}")
    public ResponseEntity<Category> getCategoryById(@PathVariable Long id) {
        return categoryService.getCategoryById(id)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    // Mettre à jour une catégorie (seulement le genre, pas les livres)
    @PutMapping("/{id}")
    public ResponseEntity<Category> updateCategory(@PathVariable Long id, @Valid @RequestBody Category updatedCategory) {
        return categoryService.updateCategory(id, updatedCategory)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    // Supprimer une catégorie
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteCategory(@PathVariable Long id) {
        boolean deleted = categoryService.deleteCategory(id);
        if (deleted) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.notFound().build();
    }

    // Récupérer une catégorie par genre (insensible à la casse, sans livres)
    @GetMapping("/genre/{genre}")
    public ResponseEntity<Category> getCategoryByGenre(@PathVariable String genre) {
        return categoryService.getCategoryByGenre(genre)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    // Récupérer tous les livres d'une catégorie
    @GetMapping("/{id}/books")
    public ResponseEntity<List<Book>> getBooksByCategory(@PathVariable Long id) {
        List<Book> books = categoryService.getBooksByCategory(id);
        if (books.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(books);
    }
}
