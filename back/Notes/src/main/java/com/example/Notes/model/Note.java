package com.example.Notes.model;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import jakarta.persistence.*;

@ToString
@Getter
@Setter
@Entity
@Table(name = "notes")
public class Note {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    private String title;
    
    @Column(columnDefinition = "TEXT")
    private String content;
    
    private String lastModified;
    
    @Column(nullable = false)
    private String author;

    public Note() {}

    public Note(Long id, String title, String content, String lastModified, String author) {
        this.id = id;
        this.title = title;
        this.content = content;
        this.lastModified = lastModified;
        this.author = author;
    }
}