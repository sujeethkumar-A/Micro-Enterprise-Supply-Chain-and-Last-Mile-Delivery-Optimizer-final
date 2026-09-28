package com.example.supplychain.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

@Entity
@Table(name = "chart_of_accounts")
public class ChartOfAccounts {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "Account name is required")
    @Column(nullable = false)
    private String name;

    @NotBlank(message = "Account code is required")
    @Column(nullable = false, unique = true)
    private String code;

    @NotNull(message = "Account category is required")
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private AccountCategory category;

    public enum AccountCategory {
        ASSET,
        LIABILITY,
        INCOME,
        EXPENSE
    }

    public ChartOfAccounts() {
    }

    public ChartOfAccounts(String name, String code, AccountCategory category) {
        this.name = name;
        this.code = code;
        this.category = category;
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public String getCode() { return code; }
    public void setCode(String code) { this.code = code; }
    public AccountCategory getCategory() { return category; }
    public void setCategory(AccountCategory category) { this.category = category; }
}
