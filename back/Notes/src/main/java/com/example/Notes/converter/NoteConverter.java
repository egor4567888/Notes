package com.example.Notes.converter;

import org.springframework.stereotype.Component;
import com.example.Notes.dto.NoteDTO;
import com.example.Notes.dto.NoteHeaderDTO;
import com.example.Notes.model.Note;

@Component
public class NoteConverter {

    public NoteDTO toNoteDTO(Note note) {
        if (note == null) return null;
        return new NoteDTO(
            note.getId(),
            note.getTitle(),
            note.getContent(),
            note.getLastModified()
        );
    }

    public NoteHeaderDTO toNoteHeaderDTO(Note note) {
        if (note == null) return null;
        return new NoteHeaderDTO(
            note.getId(),
            note.getTitle(),
            note.getLastModified()
        );
    }

    public Note toNote(NoteDTO noteDTO) {
        if (noteDTO == null) return null;
        Note note = new Note();
        note.setId(noteDTO.getId());
        note.setTitle(noteDTO.getTitle());
        note.setContent(noteDTO.getContent());
        note.setLastModified(noteDTO.getLastModified());
        return note;
    }
}