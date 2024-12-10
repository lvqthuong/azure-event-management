package com.example.fs19_azure.service;

import com.example.fs19_azure.entity.EventsAttachments;
import com.example.fs19_azure.repository.EventsAttachmentsRepository;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;
import java.util.UUID;

public class EventsAttachmentsService {
    @Autowired
    private EventsAttachmentsRepository eventsAttachmentsRepository;

    public List<EventsAttachments> getAttachmentsOfEvent(UUID eventId) {
        return eventsAttachmentsRepository.findByEventId(eventId);
    }
}
