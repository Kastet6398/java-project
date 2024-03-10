package com.example.javaapp.models.entities;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;

@Entity
public record Course(
        String title,
        String description,
        long author,
        @Id
        @GeneratedValue
        long id) {
}