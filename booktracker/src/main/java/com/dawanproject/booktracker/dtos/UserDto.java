package com.dawanproject.booktracker.dtos;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
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

    /**
     * List of review IDs (book IDs) associated with the user.
     */
    private List<Long> reviewIds;
}
