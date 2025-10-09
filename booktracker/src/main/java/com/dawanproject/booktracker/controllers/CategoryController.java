package com.dawanproject.booktracker.controllers;

import com.dawanproject.booktracker.dtos.CategoryDto;
import com.dawanproject.booktracker.entities.Category;
import com.dawanproject.booktracker.mappers.CategoryMapper;
import com.dawanproject.booktracker.services.CategoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/api/categories")
@Validated
@RequiredArgsConstructor
public class CategoryController {

    private final CategoryService categoryService;
    private final CategoryMapper categoryMapper;

    // 🔹 Créer une nouvelle catégorie
    @PostMapping
    public ResponseEntity<CategoryDto> createCategory(@Valid @RequestBody CategoryDto categoryDto) {
        Category category = categoryMapper.toEntity(categoryDto);
        Category saved = categoryService.createCategory(category);
        return ResponseEntity.status(201).body(categoryMapper.toDto(saved));
    }

    // 🔹 Récupérer toutes les catégories
    @GetMapping
    public ResponseEntity<List<CategoryDto>> getAllCategories() {
        List<CategoryDto> dtos = categoryService.getAllCategories()
                .stream()
                .map(categoryMapper::toDto)
                .toList();
        return ResponseEntity.ok(dtos);
    }

    // 🔹 Récupérer une catégorie par ID
    @GetMapping("/{id}")
    public ResponseEntity<CategoryDto> getCategoryById(@PathVariable Long id) {
        return categoryService.getCategoryById(id)
                .map(categoryMapper::toDto)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    // 🔹 Mettre à jour une catégorie (seulement le genre)
    @PutMapping("/{id}")
    public ResponseEntity<CategoryDto> updateCategory(@PathVariable Long id, @Valid @RequestBody CategoryDto updatedCategoryDto) {
        Category updatedEntity = categoryMapper.toEntity(updatedCategoryDto);
        return categoryService.updateCategory(id, updatedEntity)
                .map(categoryMapper::toDto)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    // 🔹 Supprimer une catégorie
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteCategory(@PathVariable Long id) {
        boolean deleted = categoryService.deleteCategory(id);
        return deleted ? ResponseEntity.noContent().build()
                : ResponseEntity.notFound().build();
    }

    // 🔹 Récupérer une catégorie par genre (insensible à la casse)
    @GetMapping("/genre/{genre}")
    public ResponseEntity<CategoryDto> getCategoryByGenre(@PathVariable String genre) {
        return categoryService.getCategoryByGenre(genre)
                .map(categoryMapper::toDto)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }
}
