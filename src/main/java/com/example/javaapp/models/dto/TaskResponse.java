package com.example.javaapp.models.dto;

import io.swagger.v3.oas.annotations.media.Schema;

import java.time.LocalDateTime;

public record TaskResponse(
        @Schema(description = "title")
        String title,
        @Schema(description = "description")
        String description,
        @Schema(description = "author")
        long author,
        @Schema(description = "deadline")
        LocalDateTime deadline,
        @Schema(description = "course id")
        long course_id,
        @Schema(description = "task id")
        long id) {
}
