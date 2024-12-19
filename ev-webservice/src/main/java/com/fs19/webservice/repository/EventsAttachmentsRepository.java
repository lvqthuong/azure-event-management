package com.fs19.webservice.repository;

import com.fs19.webservice.entity.EventsAttachments;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface EventsAttachmentsRepository  extends JpaRepository<EventsAttachments, UUID> {
    List<EventsAttachments> findByEventId(UUID eventId);
    Optional<EventsAttachments> findByEventIdAndId(UUID eventId, UUID id);
}
