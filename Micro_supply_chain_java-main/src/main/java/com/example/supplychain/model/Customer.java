package com.example.supplychain.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

@Entity
@Table(name = "customer")
public class Customer {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "Customer name is required")
    @Column(nullable = false)
    private String name;

    @NotBlank(message = "Phone number is required")
    @Column(nullable = false)
    private String phone;

    @Email(message = "Valid email is required")
    @Column(nullable = false)
    private String email;

    @NotBlank(message = "Address is required")
    @Column(nullable = false, length = 500)
    private String address;

    @NotBlank(message = "City is required")
    @Column(nullable = false)
    private String city;

    @NotNull(message = "Latitude is required for delivery optimization")
    @Column(nullable = false)
    private Double latitude;

    @NotNull(message = "Longitude is required for delivery optimization")
    @Column(nullable = false)
    private Double longitude;

    public Customer() {
    }

    public Customer(String name, String phone, String email, String address, String city, Double latitude, Double longitude) {
        this.name = name;
        this.phone = phone;
        this.email = email;
        this.address = address;
        this.city = city;
        this.latitude = latitude;
        this.longitude = longitude;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public Double getLatitude() {
        return latitude;
    }

    public void setLatitude(Double latitude) {
        this.latitude = latitude;
    }

    public Double getLongitude() {
        return longitude;
    }

    public void setLongitude(Double longitude) {
        this.longitude = longitude;
    }
}
