package com.example.Notes.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class NoteHeaderDTO {
    private Long id;
    private String title;
    private String lastModified;
}