package com.example.fs19_azure.entity;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
@SuperBuilder
@Entity
@Table(name = "users")
public class Users {
    @Id
    @GeneratedValue
    private UUID id;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false, unique = true)
    private String email;

    @Column(nullable = false)
    private String password;

    @Column(name = "created_at", columnDefinition = "timestamp with time zone default now()")
    private Instant createdAt;

    @Column(name = "updated_at", columnDefinition = "timestamp with time zone default now()")
    private Instant updatedAt;

    @Column(nullable = false)
    private boolean deleted;
    
    @PrePersist
    public void onPrePersist() {
        this.setCreatedAt(Instant.now());
        this.setUpdatedAt(Instant.now());
    }

    @PreUpdate
    public void onPreUpdate() {
        this.setUpdatedAt(Instant.now());
    }
}
