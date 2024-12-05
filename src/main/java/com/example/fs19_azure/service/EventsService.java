package com.example.fs19_azure.service;

import com.example.fs19_azure.dto.EventsCreate;
import com.example.fs19_azure.entity.Events;
import com.example.fs19_azure.exceptions.ErrorMessage;
import com.example.fs19_azure.exceptions.GlobalException;
import com.example.fs19_azure.repository.EventsRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class EventsService {
    @Autowired
    private EventsRepository eventsRepository;

    public Events createEvent(EventsCreate dto) {
        Events event = eventsRepository.save(
            Events.builder()
            .name(dto.name())
            .description(dto.description())
            .startDate(Instant.parse(dto.startDate()))
            .endDate(Instant.parse(dto.endDate()))
            .location(dto.location())
            .organizerId(UUID.fromString(dto.organizerId()))
            .build()
        );

        if (event == null) {
            throw new GlobalException(HttpStatus.INTERNAL_SERVER_ERROR, ErrorMessage.EVENT_CREATION_FAILED);
        }

        return event;
    }

    public Events getEvent(UUID id) {
        return eventsRepository.findById(id).orElse(null);
    }

    public List<Events> getAllEvents() {
        return eventsRepository.findAll();
    }
}
