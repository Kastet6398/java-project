package com.example.javaapp.models.entities;

import java.time.LocalDateTime;

public record Task(
        String title,
        String description,
        long author,
        LocalDateTime deadline,
        long courseId,
        long id) {
}