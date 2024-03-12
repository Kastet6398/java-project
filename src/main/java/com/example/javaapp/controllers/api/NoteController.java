package com.example.javaapp.controllers.api;

import com.example.javaapp.models.entities.Note;
import com.example.javaapp.models.services.NoteService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/notes")
public class NoteController {

    private final NoteService noteService;

    public NoteController(NoteService noteService) {
        this.noteService = noteService;
    }

    @PostMapping
    public ResponseEntity<Object> createNote(@RequestBody Note note) {
        Optional<Note> createdNote = noteService.createNote(note);
        if (createdNote.isPresent()) {
            return ResponseEntity.status(HttpStatus.CREATED).body(createdNote.get());
        } else {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Failed to create note.");
        }
    }

    @GetMapping("/{title}")
    public ResponseEntity<Object> findNoteByTitle(@PathVariable String title) {
        Optional<Note> note = noteService.findNoteByTitle(title);
        if (note.isPresent()) {
            return ResponseEntity.ok(note.get());
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/author/{authorId}")
    public ResponseEntity<Object> findNotesByAuthorId(@PathVariable Long authorId) {
        List<Note> notes = noteService.findNotesByAuthorId(authorId);
        if (!notes.isEmpty()) {
            return ResponseEntity.ok(notes);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/{noteId}")
    public ResponseEntity<Object> deleteNoteById(@PathVariable Long noteId) {
        boolean deleted = noteService.deleteNoteById(noteId);
        if (deleted) {
            return ResponseEntity.ok().build();
        } else {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Failed to delete note.");
        }
    }
}
