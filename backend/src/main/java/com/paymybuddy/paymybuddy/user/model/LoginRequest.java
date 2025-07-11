package com.paymybuddy.paymybuddy.user.model;

import jakarta.validation.constraints.*;

public record LoginRequest(
        @NotBlank @Email String email,
        @NotBlank String password) {
}
