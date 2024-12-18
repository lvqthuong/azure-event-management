package com.example.fs19_azure.repository;

import com.example.fs19_azure.entity.EventsAttachments;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface EventsAttachmentsRepository  extends JpaRepository<EventsAttachments, UUID> {
    List<EventsAttachments> findByEventId(UUID eventId);
    Optional<EventsAttachments> findByEventIdAndId(UUID eventId, UUID id);
}
