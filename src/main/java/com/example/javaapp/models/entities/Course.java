package com.example.javaapp.models.entities;

import java.util.List;

public record Course(
        String title,
        String description,
        long author,
        List<User> invitedUsers,
        long id) {
}
