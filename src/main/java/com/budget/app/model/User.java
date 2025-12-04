package com.budget.app.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "users")
public class User {

    // This is my primary key for the users table.
    // Each user in the system gets their own unique id.
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // This is the user’s display name (what I show in the UI, like “Welcome, Ealona”).
    @Column(nullable = false, length = 100)
    private String name;

    // I’m using email as the unique identifier for login.
    // unique = true so I don’t get duplicate accounts.
    @Column(nullable = false, unique = true, length = 100)
    private String email;

    // This stores the user’s password.
    // For this project I’m keeping it simple, but in a real app
    // I would store an encoded/hashed password instead of plain text.
    @Column(nullable = false, length = 255)
    private String password;

    // Empty constructor required by JPA.
    public User() {}

    // Convenience constructor I use when I’m creating a new user in my controller.
    public User(String name, String email, String password) {
        this.name = name;
        this.email = email;
        this.password = password;
    }

    // ======================= GETTERS & SETTERS =======================
    // Standard getters and setters so Spring / Thymeleaf
    // can read and update the fields when needed.

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

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
