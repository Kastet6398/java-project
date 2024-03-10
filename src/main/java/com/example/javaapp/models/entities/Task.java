package com.example.javaapp.models.entities;

import jakarta.persistence.*;

import java.time.LocalDateTime;

@Entity
public record Task(
        String title,
        String description,
        long author,
        @Temporal(TemporalType.TIMESTAMP)
        LocalDateTime deadline,
        @Id
        @GeneratedValue
        long courseId,
        long id) {
}