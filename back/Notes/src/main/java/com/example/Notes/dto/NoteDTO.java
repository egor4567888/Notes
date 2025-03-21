package com.example.Notes.dto;

public class NoteDTO {
    private Long id;
    private String title;
    private String content;
    private String lastModified;

    public NoteDTO() {}

    public NoteDTO(Long id, String title, String content, String lastModified) {
        this.id = id;
        this.title = title;
        this.content = content;
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
    public String getContent() {
        return content;
    }
    public void setContent(String content) {
        this.content = content;
    }
    public String getLastModified() {
        return lastModified;
    }
    public void setLastModified(String lastModified) {
        this.lastModified = lastModified;
    }
}