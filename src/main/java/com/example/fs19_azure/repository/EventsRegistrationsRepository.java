package com.example.fs19_azure.repository;

import com.example.fs19_azure.entity.EventsRegistrations;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface EventsRegistrationsRepository extends JpaRepository<EventsRegistrations, UUID> {
    Optional<EventsRegistrations> findByEventIdAndUserIdAndStatusIs(UUID eventId, UUID userId, String status);
}
