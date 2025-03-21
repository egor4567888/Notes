package com.example.Notes.controller;

import com.example.Notes.dto.NoteDTO;
import com.example.Notes.dto.NoteHeaderDTO;
import com.example.Notes.model.Note;
import com.example.Notes.service.NoteServiceInterface;
import com.example.Notes.converter.NoteConverter;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.LinkedHashMap;
import java.util.stream.Collectors;

@RestController
public class NoteController {

    private final NoteServiceInterface noteService;
    private final NoteConverter noteConverter;

    public NoteController(NoteServiceInterface noteService, NoteConverter noteConverter) {
        this.noteService = noteService;
        this.noteConverter = noteConverter;
    }


    // Возвращает заголовки заметок (id, title, lastModified)
    @GetMapping("${controller.endpoints.getNotesHeaders}")
    public List<NoteHeaderDTO> getNotesHeaders() {
        return noteService.getAllNoteHeaders();
    }

    // Возвращает полную заметку по id
    @GetMapping("${controller.endpoints.getNote}")
    public ResponseEntity<NoteDTO> getNote(@RequestParam("id") Long id) {
        Note note = noteService.getNoteById(id);
        if (note != null) {
            return ResponseEntity.ok(noteConverter.toNoteDTO(note));
        }
        return ResponseEntity.notFound().build();
    }

    // Сохраняет изменения существующей заметки
    @PostMapping("${controller.endpoints.saveNote}")
    public ResponseEntity<Void> saveNote(@RequestBody NoteDTO noteDto) {
        if (noteDto.getId() == null || noteService.getNoteById(noteDto.getId()) == null) {
            return ResponseEntity.badRequest().build();
        }
        Note note = noteConverter.toNote(noteDto);
        noteService.saveNote(note);
        return ResponseEntity.ok().build();
    }

    // Создает новую заметку и возвращает её id
    @PostMapping("${controller.endpoints.createNote}")
    public ResponseEntity<NoteDTO> createNote(@RequestParam("lastModified") String lastModified) {
        Note note = new Note();
        note.setLastModified(lastModified);
        Note created = noteService.createNote(note);
        return ResponseEntity.ok(noteConverter.toNoteDTO(created));
    }

    // Удаляет заметку по id
    @DeleteMapping("${controller.endpoints.deleteNote}")
    public ResponseEntity<Void> deleteNote(@RequestParam("id") Long id) {
        if (noteService.deleteNote(id)) {
            return ResponseEntity.ok().build();
        }
        return ResponseEntity.notFound().build();
    }
}