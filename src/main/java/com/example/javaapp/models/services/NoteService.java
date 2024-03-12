package com.example.javaapp.models.services;

import com.example.javaapp.models.entities.Note;
import com.example.javaapp.models.repositories.NoteRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
@Service
@Transactional(readOnly = true)
public class NoteService {
    private final NoteRepository noteRepository;

    public NoteService(NoteRepository noteRepository) {
        this.noteRepository = noteRepository;
    }
    public Optional<Note> createNote(Note note) {
        return noteRepository.createNote(note);
    }

    public Optional<Note> findNoteByTitle(String title) {
        return noteRepository.findNoteByTitle(title);
    }

    public List<Note> findNotesByAuthorId(Long authorId) {
        return noteRepository.findNotesByAuthorId(authorId);
    }

    public boolean deleteNoteById(Long noteId) {
        return noteRepository.deleteNoteById(noteId);
    }
}
