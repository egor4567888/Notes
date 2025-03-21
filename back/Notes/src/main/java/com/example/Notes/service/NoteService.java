package com.example.Notes.service;

import com.example.Notes.model.Note;
import com.example.Notes.dto.NoteHeaderDTO;
import com.example.Notes.repository.NoteRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class NoteService implements NoteServiceInterface {

    private final NoteRepository noteRepository;

    public NoteService(NoteRepository noteRepository) {
        this.noteRepository = noteRepository;
    }

    @Override
    public List<Note> getAllNotes() {
        return noteRepository.findAll();
    }

    @Override
    public Note getNoteById(Long id) {
        return noteRepository.findById(id).orElse(null);
    }

    @Override
    public Note saveNote(Note note) {
        if (note.getId() != null && noteRepository.existsById(note.getId())) {
            return noteRepository.save(note);
        }
        return null;
    }

    @Override
    public Note createNote(Note note) {
        return noteRepository.save(note);
    }

    @Override
    public boolean deleteNote(Long id) {
        if (noteRepository.existsById(id)) {
            noteRepository.deleteById(id);
            return true;
        }
        return false;
    }
    @Override
    public List<NoteHeaderDTO> getAllNoteHeaders() {
        return noteRepository.findAllHeaders();
    }
}