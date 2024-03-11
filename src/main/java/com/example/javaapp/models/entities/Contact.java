package com.example.javaapp.models.entities;

public record Contact(String name, User owner, User contact, long id) {
}
