package com.example.javaapp.models.repositories;

import com.example.javaapp.models.entities.Course;
import com.example.javaapp.models.entities.Note;
import com.example.javaapp.models.entities.Task;
import com.example.javaapp.models.entities.User;
import org.springframework.jdbc.core.simple.JdbcClient;
import org.springframework.stereotype.Repository;
import org.springframework.util.Assert;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Repository
public class NoteRepository {
    private static final String INSERT = "INSERT INTO main.note (title, content, author_id) VALUES (:title, :content, :author_id)";
    private static final String FIND_NOTE_BY_TITLE = "SELECT * FROM main.note WHERE title = :title";
    private static final String DELETE_NOTE_BY_ID = "DELETE FROM main.note WHERE note_id = :note_id";
    private static final String FIND_NOTES_BY_AUTHOR_ID = "SELECT * FROM main.note WHERE author_id = :author_id";
    private final JdbcClient jdbcClient;

    public NoteRepository(JdbcClient jdbcClient) {
        this.jdbcClient = jdbcClient;
    }
    public Optional<Note> createNote(Note note) {
        long affected= jdbcClient.sql(INSERT)
                .param("title", note.title())
                .param("content", note.content())
                .param("author_id", note.authorId())
                .update();
        if (affected == 1) {
            return findNoteByTitle(note.title());
        }
        return Optional.empty();
    }

    public Optional<Note> findNoteByTitle(String title) {
        List<Map<String, Object>> result = jdbcClient.sql(FIND_NOTE_BY_TITLE)
                .param("title", title)
                .query()
                .listOfRows();

        if (!result.isEmpty()) {
            Map<String, Object> row = result.get(0);
            return Optional.of(new Note(
                    (Long) row.get("note_id"),
                    (String) row.get("title"),
                    (String) row.get("content"),
                    null,
                    (Long) row.get("author_id")
            ));
        }
        return Optional.empty();
    }
    public boolean deleteNoteById(Long noteId) {
        return jdbcClient.sql(DELETE_NOTE_BY_ID)
                .param("note_id", noteId)
                .update() == 1;
    }
    public List<Note> findNotesByAuthorId(Long authorId) {
        List<Map<String, Object>> result = jdbcClient.sql(FIND_NOTES_BY_AUTHOR_ID)
                .param("author_id", authorId)
                .query()
                .listOfRows();
        List<Note> notes = new ArrayList<>();
        for (Map<String, Object> row : result) {
            notes.add(new Note(
                    (Long) row.get("note_id"),
                    (String) row.get("title"),
                    (String) row.get("content"),
                    null,
                    (Long) row.get("author_id")
            ));
        }
        return notes;
    }
}
