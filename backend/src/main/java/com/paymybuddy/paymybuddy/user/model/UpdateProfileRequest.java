package com.paymybuddy.paymybuddy.user.model;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record UpdateProfileRequest(
        @NotBlank @Size(max = 50) String username,
        @NotBlank @Email String email,
        // Si non nul/non vide => on remplace l'ancien mot de passe
        @Size(min = 6) String password) {
}
