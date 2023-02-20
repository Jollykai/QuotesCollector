package com.jollykai.quotescollector.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "users")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @NotBlank
    private String name;
    @NotBlank
    private String email;
    @NotBlank
    private String password;
    private LocalDateTime creationDate;
    @ElementCollection
    private List<Long> votedQuotes;

    public User() {
    }

    public User(String name, String email, String password, LocalDateTime localDateTime) {
        this.name = name;
        this.email = email;
        this.password = password;
        this.creationDate = localDateTime;
        this.votedQuotes = new ArrayList<>();
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

    public LocalDateTime getCreationDate() {
        return creationDate;
    }

    public void setCreationDate(LocalDateTime creationDate) {
        this.creationDate = creationDate;
    }

    public List<Long> getVotedQuotes() {
        return votedQuotes;
    }

    public void setVotedQuotes(List<Long> votedQuotes) {
        this.votedQuotes = votedQuotes;
    }
}





