package com.fs19.webservice.entity;

import io.hypersistence.utils.hibernate.type.json.JsonType;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;
import org.hibernate.annotations.Type;

import java.time.Instant;
import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
@SuperBuilder
@Entity
@Table(name = "events")
public class Events {
    @Id
    @GeneratedValue
    private UUID id;

    @Column(nullable = false)
    private String type;

    @Column(nullable = false)
    private String name;

    @Column
    private String description;

    @Column(nullable = false)
    private String location;

    @Column(nullable = false)
    private Instant startDate;

    @Column(nullable = false)
    private Instant endDate;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "organizer_id", nullable = false)
    private Users organizer;

    @Type(JsonType.class)
    @Column(columnDefinition = "jsonb")
    private String metadata;

    @Column
    private boolean deleted;

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