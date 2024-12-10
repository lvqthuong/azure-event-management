package com.example.fs19_azure.service;

import com.example.fs19_azure.dto.EventsCreate;
import com.example.fs19_azure.dto.EventsRead;
import com.example.fs19_azure.dto.EventsUpdate;
import com.example.fs19_azure.dto.mapper.EventsMapper;
import com.example.fs19_azure.entity.Events;
import com.example.fs19_azure.entity.Users;
import com.example.fs19_azure.exceptions.ErrorMessage;
import com.example.fs19_azure.exceptions.GlobalException;
import com.example.fs19_azure.repository.EventsRepository;
import com.example.fs19_azure.repository.UsersRepository;
import jakarta.transaction.Transactional;
import org.hibernate.Hibernate;
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

    @Autowired
    private UsersRepository usersRepository;

    public Events createEvent(EventsCreate dto) {
        //fetch the organizer
        Users organizer = usersRepository.findById(UUID.fromString(dto.organizerId()))
            .orElseThrow(() -> new GlobalException(HttpStatus.NOT_FOUND, ErrorMessage.USER_NOT_FOUND));

        Events event = eventsRepository.save(
            Events.builder()
            .name(dto.name())
            .type(dto.type())
            .description(dto.description())
            .startDate(Instant.parse(dto.startDate()))
            .endDate(Instant.parse(dto.endDate()))
            .location(dto.location())
            .organizer(organizer)
            .metadata(dto.metadata())
            .build()
        );

        if (event == null) {
            throw new GlobalException(HttpStatus.BAD_REQUEST, ErrorMessage.EVENT_CREATION_FAILED);
        }

        return event;
    }

    public Events getEvent(UUID id) {
        return eventsRepository.findById(id).orElse(null);
    }

    public List<Events> getAllActiveEvents() {
        return eventsRepository.findByDeletedFalse();
    }

    @Transactional
    public EventsRead updateEvent(UUID id, EventsUpdate dto) {
        Optional<Events> existingEvent = eventsRepository.findById(id);
        if (existingEvent.isEmpty()) {
            throw new GlobalException(HttpStatus.NOT_FOUND, ErrorMessage.EVENT_NOT_FOUND);
        }

        //fetch the organizer
        Hibernate.initialize(existingEvent.get().getOrganizer());

        //update the event
        EventsMapper.INSTANCE.toEventsFromEventsUpdate(dto, existingEvent.get());

        //save the updated event
        Events updatedEvent = eventsRepository.save(existingEvent.get());

        if (updatedEvent == null) {
            throw new GlobalException(HttpStatus.BAD_REQUEST, ErrorMessage.EVENT_UPDATE_FAILED);
        }

        return EventsMapper.toEventsRead(updatedEvent);
    }

    @Transactional
    public int deleteEvent(UUID id) {
        int result = eventsRepository.deleteByIdSoft(id);
        if (result == 0) {
            throw new GlobalException(HttpStatus.NOT_FOUND, ErrorMessage.EVENT_NOT_FOUND);
        }

        return result;
    }
}
