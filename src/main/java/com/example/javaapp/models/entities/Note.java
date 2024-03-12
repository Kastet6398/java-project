package com.example.javaapp.models.entities;


public record Note(Long noteId, String title, String content, User author, Long authorId) {

}
