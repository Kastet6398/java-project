package com.example.javaapp.models.dto;

import com.example.javaapp.models.entities.User;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;

import java.util.List;

public record ContactRequest(
        @NotBlank(message = "Name cannot be blank")
        String name,
        @NotBlank(message = "Id cannot be blank")
        Long id) {
}

