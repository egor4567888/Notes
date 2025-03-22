package com.example.Notes.service;

import com.example.Notes.model.Note;
import com.example.Notes.dto.NoteHeaderDTO;
import com.example.Notes.repository.NoteRepository;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class NoteService implements NoteServiceInterface {

    private final NoteRepository noteRepository;

    public NoteService(NoteRepository noteRepository) {
        this.noteRepository = noteRepository;
    }

    // Возвращает заметки текущего пользователя
    @Override
    public List<Note> getAllNotes() {
        String username = getCurrentUsername();
        return noteRepository.findAll().stream()
                .filter(note -> username.equals(note.getAuthor()))
                .toList();
    }

    // Возвращает заметку по id, если она принадлежит текущему пользователю
    @Override
    public Note getNoteById(Long id) {
        String username = getCurrentUsername();
        Optional<Note> noteOpt = noteRepository.findByIdAndAuthor(id, username);
        return noteOpt.orElse(null);
    }

    // Обновление заметки, если она принадлежит текущему пользователю
    @Override
    public Note saveNote(Note note) {
        String username = getCurrentUsername();
        if (note.getId() != null) {
            Optional<Note> existing = noteRepository.findByIdAndAuthor(note.getId(), username);
            if (existing.isPresent()) {
                note.setAuthor(username);
                return noteRepository.save(note);
            }
        }
        return null;
    }

    // При создании заметки устанавливаем текущего пользователя как автора
    @Override
    public Note createNote(Note note) {
        note.setAuthor(getCurrentUsername());
        return noteRepository.save(note);
    }

    // Удаление заметки, если она принадлежит текущему пользователю
    @Override
    public boolean deleteNote(Long id) {
        String username = getCurrentUsername();
        Optional<Note> existing = noteRepository.findByIdAndAuthor(id, username);
        if (existing.isPresent()) {
            noteRepository.deleteById(id);
            return true;
        }
        return false;
    }
    
    // Новый метод для получения заголовков заметок текущего пользователя
    @Override
    public List<NoteHeaderDTO> getAllNoteHeaders() {
        String username = getCurrentUsername();
        return noteRepository.findAllHeadersByAuthor(username);
    }
    
    private String getCurrentUsername() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        return (auth != null) ? auth.getName() : null;
    }
}