package com.example.javaapp.models.dto;

import io.swagger.v3.oas.annotations.media.Schema;

public record CourseResponse(
        @Schema(description = "title")
        String title,
        @Schema(description = "description")
        String description,
        @Schema(description = "author")
        long author,
        @Schema(description = "task id")
        long id) {
}
