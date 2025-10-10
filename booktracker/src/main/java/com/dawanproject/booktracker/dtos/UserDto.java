package com.dawanproject.booktracker.dtos;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * DTO for representing User data for creation, update, and retrieval.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserDto {

    /**
     * Unique identifier for the user.
     */
    @NotNull(message = "L'ID utilisateur est requis pour les mises à jour")
    private Long userId;

    /**
     * Username of the user.
     */
    @NotBlank(message = "Le nom d'utilisateur est requis")
    private String username;

    /**
     * Email address of the user.
     */
    @Email(message = "L'email doit être valide")
    @NotBlank(message = "L'email est requis")
    private String email;

    /**
     * Password of the user (used for creation/update, not returned in responses).
     */
    @NotBlank(message = "Le mot de passe est requis")
    private String password;

    /**
     * Indicates whether the user is subscribed to a service.
     */
    private boolean isSubscribed;

    private List<Long> bookIds;

    /**
     * List of review IDs (book IDs) associated with the user.
     */
    @NotNull(message = "La liste des IDs de critiques ne peut pas être nulle")
    private List<Long> reviewIds;
}
