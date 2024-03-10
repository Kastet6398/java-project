package com.example.javaapp.models.dto;

import jakarta.validation.constraints.NotBlank;

import java.time.LocalDateTime;

public record CourseRequest(
        @NotBlank(message = "Title cannot be blank")
        String title,
        @NotBlank(message = "Description cannot be blank")
        String description) {
}
