package com.example.supplychain.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "artisan_cluster")
public class ArtisanCluster {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "Cluster name is required")
    @Column(nullable = false, unique = true)
    private String name;

    @Column(length = 1000)
    private String description;

    @OneToMany(mappedBy = "cluster", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ArtisanContact> contacts = new ArrayList<>();

    public ArtisanCluster() {
    }

    public ArtisanCluster(String name, String description) {
        this.name = name;
        this.description = description;
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }
    public List<ArtisanContact> getContacts() { return contacts; }
    public void setContacts(List<ArtisanContact> contacts) { this.contacts = contacts; }
}
