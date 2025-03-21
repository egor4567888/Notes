package com.example.Notes.model;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Note {
    private Long id;
    private String title;
    private String content;
    private String lastModified;

    public Note() {}

    public Note(Long id, String title, String content, String lastModified) {
        this.id = id;
        this.title = title;
        this.content = content;
        this.lastModified = lastModified;
    }
}
