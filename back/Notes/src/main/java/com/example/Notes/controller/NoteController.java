package com.example.Notes.controller;

import com.example.Notes.dto.NoteDTO;
import com.example.Notes.dto.NoteHeaderDTO;
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
    public List<NoteHeaderDTO> getNotesHeaders() {
        return noteService.getAllNotes().stream()
                .map(note -> new NoteHeaderDTO(
                        note.getId(),
                        note.getTitle(),
                        note.getLastModified()
                ))
                .collect(Collectors.toList());
    }

    // Возвращает полную заметку по id
    @GetMapping("/getNote")
    public ResponseEntity<NoteDTO> getNote(@RequestParam("id") Long id) {
        Note note = noteService.getNoteById(id);
        if (note != null) {
            NoteDTO dto = new NoteDTO(note.getId(), note.getTitle(), note.getContent(), note.getLastModified());
            return ResponseEntity.ok(dto);
        }
        return ResponseEntity.notFound().build();
    }

    // Сохраняет изменения существующей заметки
    @PostMapping("/saveNote")
    public ResponseEntity<Void> saveNote(@RequestBody NoteDTO noteDto) {
        if (noteDto.getId() == null || noteService.getNoteById(noteDto.getId()) == null) {
            return ResponseEntity.badRequest().build();
        }
        Note note = new Note();
        note.setId(noteDto.getId());
        note.setTitle(noteDto.getTitle());
        note.setContent(noteDto.getContent());
        note.setLastModified(noteDto.getLastModified());
        noteService.saveNote(note);
        return ResponseEntity.ok().build();
    }

    // Создает новую заметку и возвращает её id
    @PostMapping("/createNote")
    public ResponseEntity<NoteDTO> createNote(@RequestParam("lastModified") String lastModified) {
        Note note = new Note();
        note.setLastModified(lastModified);
        Note created = noteService.createNote(note);
        NoteDTO dto = new NoteDTO(created.getId(), created.getTitle(), created.getContent(), created.getLastModified());
        return ResponseEntity.ok(dto);
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