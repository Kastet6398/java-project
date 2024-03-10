package com.example.javaapp.models.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDateTime;

public record TaskRequest(
        @NotBlank(message = "Title cannot be blank")
        @NotNull(message = "Title cannot be null")
        String title,
        @NotBlank(message = "Description cannot be blank")
        @NotNull(message = "Description cannot be null")
        String description,
        @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
        @NotNull(message = "Deadline cannot be null")
        LocalDateTime deadline,
        @NotNull(message = "Course ID cannot be null")
        long courseId) {
}
