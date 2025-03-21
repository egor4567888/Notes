package com.example.Notes.repository;

import com.example.Notes.dto.NoteHeaderDTO;
import com.example.Notes.model.Note;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface NoteRepository extends JpaRepository<Note, Long> {

    // Возвращает заголовки заметок
    @Query("SELECT new com.example.Notes.dto.NoteHeaderDTO(n.id, n.title, n.lastModified) FROM Note n")
    List<NoteHeaderDTO> findAllHeaders();
}