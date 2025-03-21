package com.example.Notes.service;

import com.example.Notes.model.Note;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;

@Service
public class NoteService {
    private final Map<Long, Note> noteMap = new ConcurrentHashMap<>();
    private final AtomicLong idGenerator = new AtomicLong(1);

    public List<Note> getAllNotes() {
        return new ArrayList<>(noteMap.values());
    }

    public Note getNoteById(Long id) {
        return noteMap.get(id);
    }

    public Note saveNote(Note note) {
        if (note.getId() != null && noteMap.containsKey(note.getId())) {
            noteMap.put(note.getId(), note);
            return note;
        }
        return null;
    }

    public Note createNote(Note note) {
        Long id = idGenerator.getAndIncrement();
        note.setId(id);
        noteMap.put(id, note);
        return note;
    }

    public boolean deleteNote(Long id) {
        return noteMap.remove(id) != null;
    }
}