package com.example.javaapp.models.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record ContactRequest(
        @NotBlank(message = "Name cannot be blank")
        String name,
        @NotNull(message = "Id cannot be blank")
        Long id) {
}

