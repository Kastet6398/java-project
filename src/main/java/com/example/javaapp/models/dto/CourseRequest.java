package com.example.javaapp.models.dto;

import jakarta.validation.constraints.NotBlank;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public record CourseRequest(
        @NotBlank(message = "Title cannot be blank")
        String title,
        List<Long> invitedUsers,
        @NotBlank(message = "Description cannot be blank")
        String description) {
}
