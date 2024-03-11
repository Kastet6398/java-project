package com.example.javaapp.models.dto;

import io.swagger.v3.oas.annotations.media.Schema;

public record UserResponse(
        @Schema(description = "name")
        String name,
        @Schema(description = "email")
        String email,
        @Schema(description = "phone")
        String phone) {
}
