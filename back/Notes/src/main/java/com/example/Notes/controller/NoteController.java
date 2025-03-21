package com.example.Notes.controller;

import com.example.Notes.model.Note;
import com.example.Notes.service.NoteServiceInterface;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.LinkedHashMap;
import java.util.stream.Collectors;

@RestController
public class NoteController {

    private final NoteServiceInterface noteService;

    public NoteController(NoteServiceInterface noteService) {
        this.noteService = noteService;
    }

    // Возвращает заголовки заметок (id, title, lastModified)
    @GetMapping("/getNotesHeaders")
    public List<Map<String, Object>> getNotesHeaders() {
        return noteService.getAllNotes().stream()
                .map(note -> {
                    Map<String, Object> noteMap = new LinkedHashMap<>();
                    noteMap.put("id", note.getId());
                    noteMap.put("title", note.getTitle());
                    noteMap.put("lastModified", note.getLastModified());
                    return noteMap;
                })
                .collect(Collectors.toList());
    }

    // Возвращает полную заметку по id
    @GetMapping("/getNote")
    public ResponseEntity<Note> getNote(@RequestParam("id") Long id) {
        Note note = noteService.getNoteById(id);
        if (note != null) {
            return ResponseEntity.ok(note);
        }
        return ResponseEntity.notFound().build();
    }

    // Сохраняет изменения существующей заметки
    @PostMapping("/saveNote")
    public ResponseEntity<Void> saveNote(@RequestBody Note note) {
        if (note.getId() == null || noteService.getNoteById(note.getId()) == null) {
            return ResponseEntity.badRequest().build();
        }
        noteService.saveNote(note);
        return ResponseEntity.ok().build();
    }

    // Создает новую заметку и возвращает её id
    @PostMapping("/createNote")
    public Map<String, Object> createNote(@RequestBody Note note) {
        Note created = noteService.createNote(note);
        return Map.of("id", created.getId());
    }

    // Удаляет заметку по id
    @DeleteMapping("/deleteNote")
    public ResponseEntity<Void> deleteNote(@RequestParam("id") Long id) {
        if (noteService.deleteNote(id)) {
            return ResponseEntity.ok().build();
        }
        return ResponseEntity.notFound().build();
    }
}