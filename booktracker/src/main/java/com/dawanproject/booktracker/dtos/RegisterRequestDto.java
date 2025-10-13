package com.dawanproject.booktracker.dtos;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record RegisterRequestDto(
        @NotBlank(message = "Le prénom est requis") @Size(max = 50, message = "Le prénom ne peut pas dépasser 50 caractères") String firstname,
        @NotBlank(message = "Le nom de famille est requis") @Size(max = 50, message = "Le nom de famille ne peut pas dépasser 50 caractères") String lastname,
        @NotBlank(message = "Le nom d'utilisateur est requis") String username,
        @NotBlank(message = "L'email est requis") @Email(message = "L'email doit être valide") String email,
        @NotBlank(message = "Le mot de passe est requis") String password) {
}

