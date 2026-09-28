package com.example.supplychain.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "journal")
public class Journal {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    @Column(name = "journal_type", nullable = false)
    private String journalType;

    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt = LocalDateTime.now();

    public Journal() {
    }

    public Journal(String name, String journalType) {
        this.name = name;
        this.journalType = journalType;
        this.createdAt = LocalDateTime.now();
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public String getJournalType() { return journalType; }
    public void setJournalType(String journalType) { this.journalType = journalType; }
    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }
}
