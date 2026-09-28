package com.example.supplychain.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

@Entity
@Table(name = "artisan_contact")
public class ArtisanContact {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "Artisan name is required")
    @Column(nullable = false)
    private String name;

    @NotBlank(message = "Phone is required")
    @Column(nullable = false)
    private String phone;

    @Email(message = "Valid email is required")
    @Column(nullable = false)
    private String email;

    @NotBlank(message = "Location is required")
    @Column(nullable = false)
    private String location;

    @NotNull(message = "Contact type is required")
    @Enumerated(EnumType.STRING)
    @Column(name = "contact_type", nullable = false)
    private ContactType contactType = ContactType.ARTISAN;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "cluster_id")
    private ArtisanCluster cluster;

    public enum ContactType {
        ARTISAN,
        MERCHANT,
        DELIVERY_PARTNER
    }

    public ArtisanContact() {
    }

    public ArtisanContact(String name, String phone, String email, String location, ContactType contactType, ArtisanCluster cluster) {
        this.name = name;
        this.phone = phone;
        this.email = email;
        this.location = location;
        this.contactType = contactType;
        this.cluster = cluster;
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public String getPhone() { return phone; }
    public void setPhone(String phone) { this.phone = phone; }
    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }
    public String getLocation() { return location; }
    public void setLocation(String location) { this.location = location; }
    public ContactType getContactType() { return contactType; }
    public void setContactType(ContactType contactType) { this.contactType = contactType; }
    public ArtisanCluster getCluster() { return cluster; }
    public void setCluster(ArtisanCluster cluster) { this.cluster = cluster; }
}
