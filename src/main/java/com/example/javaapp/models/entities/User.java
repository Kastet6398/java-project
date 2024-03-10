package com.example.javaapp.models.entities;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;

@Entity
public record User(String name,
                   String email,
                   String password,
                   String phone,
                   @Id
                   @GeneratedValue
                   long id) {
}
