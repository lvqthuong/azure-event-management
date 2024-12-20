package com.fs19.webservice.repository;

import com.fs19.webservice.entity.EventsRegistrations;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface EventsRegistrationsRepository extends JpaRepository<EventsRegistrations, UUID> {
    Optional<EventsRegistrations> findByEventIdAndUserIdAndStatus(UUID eventId, UUID userId, String status);
}
