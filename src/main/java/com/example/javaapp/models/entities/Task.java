package com.example.javaapp.models.entities;

public record Task(String title,
                   String description,
                   String author,
                   String createdAt,
                   String deadline,
                   String task_id,
                   String course_id) {
}