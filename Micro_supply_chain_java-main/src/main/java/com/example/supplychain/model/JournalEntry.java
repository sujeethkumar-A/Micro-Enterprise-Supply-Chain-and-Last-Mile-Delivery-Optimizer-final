package com.example.supplychain.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "journal_entry")
public class JournalEntry {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "journal_id", nullable = false)
    private Journal journal;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "account_id", nullable = false)
    private ChartOfAccounts account;

    @Column(nullable = false)
    private Double amount;

    @Column(name = "entry_type", nullable = false)
    private String entryType;

    @Column(name = "reference_type")
    private String referenceType;

    @Column(name = "reference_id")
    private Long referenceId;

    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt = LocalDateTime.now();

    public JournalEntry() {
    }

    public JournalEntry(Journal journal, ChartOfAccounts account, Double amount, String entryType, String referenceType, Long referenceId) {
        this.journal = journal;
        this.account = account;
        this.amount = amount;
        this.entryType = entryType;
        this.referenceType = referenceType;
        this.referenceId = referenceId;
        this.createdAt = LocalDateTime.now();
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public Journal getJournal() { return journal; }
    public void setJournal(Journal journal) { this.journal = journal; }
    public ChartOfAccounts getAccount() { return account; }
    public void setAccount(ChartOfAccounts account) { this.account = account; }
    public Double getAmount() { return amount; }
    public void setAmount(Double amount) { this.amount = amount; }
    public String getEntryType() { return entryType; }
    public void setEntryType(String entryType) { this.entryType = entryType; }
    public String getReferenceType() { return referenceType; }
    public void setReferenceType(String referenceType) { this.referenceType = referenceType; }
    public Long getReferenceId() { return referenceId; }
    public void setReferenceId(Long referenceId) { this.referenceId = referenceId; }
    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }
}
