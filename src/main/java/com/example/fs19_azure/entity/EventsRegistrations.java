package com.example.fs19_azure.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.time.Instant;
import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
@SuperBuilder
@Entity
@Table(name = "events_registrations")
public class EventsRegistrations {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "event_id", nullable = false)
    private Events event;
/*
 *  Remove the relationship with the Users entity
 *
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private Users user;
*/
    @Column(nullable = false)
    private UUID userId;

    @Column(nullable = false)
    private EventsRegistrationsStatus status; // e.g., "attending", "declined"

    @Column(name = "created_at", columnDefinition = "timestamp with time zone default now()")
    private Instant createdAt;

    @Column(name = "updated_at", columnDefinition = "timestamp with time zone default now()")
    private Instant updatedAt;

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
