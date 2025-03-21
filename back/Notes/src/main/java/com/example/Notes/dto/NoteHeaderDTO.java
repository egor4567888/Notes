package com.example.Notes.dto;

public class NoteHeaderDTO {
    private Long id;
    private String title;
    private String lastModified;

    public NoteHeaderDTO() {}

    public NoteHeaderDTO(Long id, String title, String lastModified) {
        this.id = id;
        this.title = title;
        this.lastModified = lastModified;
    }

    public Long getId() {
        return id;
    }
    public void setId(Long id) {
        this.id = id;
    }
    public String getTitle() {
        return title;
    }
    public void setTitle(String title) {
        this.title = title;
    }
    public String getLastModified() {
        return lastModified;
    }
    public void setLastModified(String lastModified) {
        this.lastModified = lastModified;
    }
}