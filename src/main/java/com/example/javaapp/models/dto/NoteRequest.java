package com.example.javaapp.models.dto;

import io.swagger.v3.oas.annotations.media.Schema;

public record NoteRequest(
        @Schema(description = "Note Id")
        Long noteId,
        @Schema(description = "Note title")
        String title,
        @Schema(description = "Note content")
        String content,
        @Schema(description = "Note author")
        Long author) {

    public NoteRequest() {
        this(null, null, null, null);
    }
}

