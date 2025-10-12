package com.dawanproject.booktracker.dtos;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class RegisterRequestDto {

    @NotBlank(message = "Le prénom est requis")
    @Size(max = 50, message = "Le prénom ne peut pas dépasser 50 caractères")
    private String firstname;

    @NotBlank(message = "Le nom de famille est requis")
    @Size(max = 50, message = "Le nom de famille ne peut pas dépasser 50 caractères")
    private String lastname;

    @NotBlank(message = "Le nom d'utilisateur est requis")
    private String username;

    @Email(message = "L'email doit être valide")
    @NotBlank(message = "L'email est requis")
    private String email;

    @NotBlank(message = "Le mot de passe est requis")
    private String password;
}

