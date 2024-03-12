package com.example.javaapp.models.dto;

import io.swagger.v3.oas.annotations.media.Schema;

public record NoteResponse(
        @Schema(description = "Note id")
        Long noteId,
        @Schema(description = "Note title")
        String title,
        @Schema(description = "Note content")
        String content,
        @Schema(description = "Note author")
        UserResponse author,
        @Schema(description = "Note authorId")
        Long authorId) {

}
