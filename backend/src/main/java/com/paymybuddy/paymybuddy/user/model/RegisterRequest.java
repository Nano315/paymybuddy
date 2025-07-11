package com.paymybuddy.paymybuddy.user.model;

import jakarta.validation.constraints.*;

public record RegisterRequest(
        @NotBlank @Size(max = 50) String username,
        @NotBlank @Email String email,
        @NotBlank @Size(min = 6) String password) {
}
