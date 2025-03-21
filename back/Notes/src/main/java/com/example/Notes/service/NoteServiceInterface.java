package com.example.Notes.service;

import com.example.Notes.model.Note;
import com.example.Notes.dto.NoteHeaderDTO;
import java.util.List;

public interface NoteServiceInterface {
    List<Note> getAllNotes();
    Note getNoteById(Long id);
    Note saveNote(Note note);
    Note createNote(Note note);
    boolean deleteNote(Long id);
    public List<NoteHeaderDTO> getAllNoteHeaders();
}