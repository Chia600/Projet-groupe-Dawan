package com.dawanproject.booktracker.mappers;

import com.dawanproject.booktracker.dtos.BookDto;
import com.dawanproject.booktracker.entities.Book;
import org.mapstruct.*;
import org.mapstruct.factory.Mappers;

import java.time.format.DateTimeFormatter;

/**
 * Mapper MapStruct permettant la conversion entre l'entité {@link Book}
 * et son DTO {@link BookDto}.
 * <p>
 * Généré automatiquement par MapStruct à la compilation,
 * ce mapper simplifie la conversion entre les objets de la base de données
 * et ceux exposés par l'API.
 * <p>
 * Il contient également des méthodes utilitaires pour le formatage
 * (dates, auteurs, catégories, etc.).
 */
// Indique à MapStruct que c'est une interface de mapping
// et qu'il doit générer une implémentation utilisable par Spring
@Mapper(componentModel = "spring")
public interface BookMapper {

    // Permet d'obtenir une instance manuelle du mapper (utile si pas injecté par Spring)
    BookMapper INSTANCE = Mappers.getMapper(BookMapper.class);

    // Définit un formateur de date ISO standard (ex: "2024-10-07")
    DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ISO_LOCAL_DATE;

    /**
     * Convertit une entité {@link Book} en {@link BookDto}.
     * <p>
     * Cette méthode mappe explicitement chaque champ et applique des conversions
     * personnalisées via {@link #formatDate(java.time.LocalDate)},
     * {@link #formatAuthor(Book)} et {@link #formatCategory(Book)}.
     *
     * @param book entité Book à convertir
     * @return DTO correspondant, ou {@code null} si l’entrée est {@code null}
     */
    // --- Book → BookDto ---
    // Cette méthode décrit comment transformer une entité Book vers un DTO BookDto
    @Mapping(target = "id", source = "book.bookId")                       // mappe book.bookId → dto.id
    @Mapping(target = "idVolume", source = "book.idVolume")               // mappe le volumeId API
    @Mapping(target = "title", source = "book.title")                     // mappe le titre
    @Mapping(target = "description", source = "book.description")         // mappe la description
    @Mapping(target = "pageNumber", source = "book.pageNumber")           // mappe le nombre de pages
    @Mapping(target = "cover", source = "book.cover")                     // mappe l’URL de couverture
    @Mapping(target = "publicationDate", expression = "java(formatDate(book.getPublicationDate()))")
    // formate la date en String via la méthode utilitaire formatDate()
    @Mapping(target = "author", expression = "java(formatAuthor(book))")
    // appelle formatAuthor() pour combiner prénom + nom
    @Mapping(target = "category", expression = "java(formatCategory(book))")
    // appelle formatCategory() pour récupérer le genre
    BookDto toDto(Book book);  // génère un BookDto à partir de l'entité Book

    /**
     * Convertit un {@link BookDto} en entité {@link Book}.
     * <p>
     * L'annotation {@link InheritInverseConfiguration} indique à MapStruct
     * d’utiliser les mêmes règles de mapping, mais dans le sens inverse.
     *
     * @param dto DTO à convertir
     * @return entité Book correspondante
     */
    // --- BookDto → Book (inverse du précédent) ---
    // @InheritInverseConfiguration dit à MapStruct d’utiliser les mêmes mappings, inversés
    @InheritInverseConfiguration
    Book toEntity(BookDto dto);

    // --- Méthodes utilitaires ---

    /**
     * Formate la date au format texte ISO (ex: "2025-10-07"),
     * ou renvoie {@code null} si la date est absente.
     *
     * @param date la date à formater
     * @return chaîne de caractères représentant la date
     */
    // Formate la date en texte ISO (ex: "2025-10-07") ou renvoie null si absente
    default String formatDate(java.time.LocalDate date) {
        return date != null ? DATE_FORMATTER.format(date) : null;
    }

    /**
     * Concatène le prénom et le nom de l'auteur, avec gestion des valeurs nulles.
     * Si aucun auteur n'est défini, renvoie {@code null}.
     *
     * @param book le livre contenant l’auteur
     * @return nom complet formaté ou {@code null} si non défini
     */
    // Concatène prénom + nom d'auteur (avec gestion des valeurs nulles)
    default String formatAuthor(Book book) {
        if (book == null || book.getAuthor() == null) return null;
        var a = book.getAuthor();
        String name = (nullSafe(a.getFirstname()) + " " + nullSafe(a.getLastname())).trim();
        return name.isBlank() ? null : name;
    }

    /**
     * Extrait le genre depuis la catégorie associée au livre.
     *
     * @param book le livre à inspecter
     * @return genre de la catégorie, ou {@code null} si absent
     */
    // Extrait le genre depuis la catégorie associée au livre
    default String formatCategory(Book book) {
        if (book == null || book.getCategory() == null) return null;
        return book.getCategory().getGenre();
    }

    /**
     * Retourne une chaîne vide si la valeur est {@code null}.
     * <p>
     * Utile pour éviter les {@link NullPointerException} lors des concaténations.
     *
     * @param value texte à vérifier
     * @return chaîne vide si {@code null}, sinon la valeur d’origine
     */
    // Retourne une chaîne vide si la valeur est null (évite les NullPointerException)
    default String nullSafe(String value) {
        return value == null ? "" : value;
    }
}
