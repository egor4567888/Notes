package com.example.Notes.controller;

import com.example.Notes.dto.NoteDTO;
import com.example.Notes.dto.NoteHeaderDTO;
import com.example.Notes.model.Note;
import com.example.Notes.service.NoteServiceInterface;
import com.example.Notes.converter.NoteConverter;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.web.bind.annotation.*;

@RestController
public class NoteController {

    private final NoteServiceInterface noteService;
    private final NoteConverter noteConverter;
    private final SimpMessagingTemplate messagingTemplate;

    public NoteController(NoteServiceInterface noteService, NoteConverter noteConverter, SimpMessagingTemplate messagingTemplate) {
        this.noteService = noteService;
        this.noteConverter = noteConverter;
        this.messagingTemplate = messagingTemplate;
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
        NoteDTO updatedDto = noteConverter.toNoteDTO(note);
        // Рассылка обновление всем подписанным на топик "/topic/note"
        messagingTemplate.convertAndSend("/topic/note", updatedDto);
        return ResponseEntity.ok().build();
    }

    // Создает новую заметку и возвращает её id lastModified
    @PostMapping("${controller.endpoints.createNote}")
    public ResponseEntity<NoteDTO> createNote(@RequestBody NoteDTO noteDto) {
        Note note = noteConverter.toNote(noteDto);
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