package com.example.javaapp.models.dto;

import io.swagger.v3.oas.annotations.media.Schema;

import java.util.List;

public record CourseResponse(
        @Schema(description = "title")
        String title,
        List<UserResponse> invitedUsers,
        @Schema(description = "description")
        String description,
        @Schema(description = "author")
        long author,
        @Schema(description = "task id")
        long id) {
}
