package com.example.fs19_azure.service;

import com.example.fs19_azure.dto.*;
import com.example.fs19_azure.dto.mapper.EventsMapper;
import com.example.fs19_azure.entity.Events;
import com.example.fs19_azure.entity.Users;
import com.example.fs19_azure.exceptions.ErrorMessage;
import com.example.fs19_azure.exceptions.GlobalException;
import com.example.fs19_azure.repository.EventsRepository;
import com.example.fs19_azure.repository.UsersRepository;

import com.example.fs19_azure.service.redis.EventsRedisService;
import jakarta.transaction.Transactional;
import org.hibernate.Hibernate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.*;

@Service
public class EventsService {

    @Autowired
    private EventsRepository eventsRepository;

    @Autowired
    private UsersRepository usersRepository;

    @Autowired
    private EventsAttachmentsService eventsAttachmentsService;

    @Autowired
    private EventsRedisService eventsCachingService;

    private static final Logger logger = LoggerFactory.getLogger(EventsService.class);

    public EventsWithAttachments createEvent(EventsCreate dto) {
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

        //store in cache
        EventsWithAttachments eventsWithAttachments = EventsMapper.toEventsWithAttachments(event, new ArrayList<>());
        eventsCachingService.saveEvent(eventsWithAttachments.id(), eventsWithAttachments);

        logger.info("Event created: {}", eventsWithAttachments.id());
        return eventsWithAttachments;
    }

    public EventsWithAttachments getEvent(UUID id) {

        EventsWithAttachments eventFromCache = eventsCachingService.getEvent(id.toString());

        //cache hit
        if (eventFromCache != null) {
            return eventFromCache;
        }

        //cache miss
        // -> retrieve event from db
        Optional<Events> event = eventsRepository.findById(id);
        if (event.isEmpty()) {
            throw new GlobalException(HttpStatus.NOT_FOUND, ErrorMessage.EVENT_NOT_FOUND);
        }

        // -> retrieve event attachments
        List<UploadedAttachment> attachments = eventsAttachmentsService.getAttachmentsOfEvent(id);

        // -> convert to DTO
        eventFromCache = EventsMapper.toEventsWithAttachments(event.get(), attachments);

        // -> store in cache
        eventsCachingService.saveEvent(eventFromCache.id(), eventFromCache);

        return eventFromCache;
    }

    public List<EventsWithAttachments> getAllActiveEvents() {
        //retrieve all events from cache
        List<EventsWithAttachments> eventsFromCache = eventsCachingService.getAllEvents();

        // cache hit
        if (eventsFromCache.size() > 0) {
            System.out.println("Events cache hit, return!");
            return eventsFromCache;
        }

        // cache miss, fetch from database
        List<Events> eventsFromDB = eventsRepository.findByDeletedFalse();
        List<EventsWithAttachments> eventsWithAttachments = new ArrayList<>();

        for (Events event : eventsFromDB) {
            //store the event with empty attachments to cache
            EventsWithAttachments e = EventsMapper.toEventsWithAttachments(event, new ArrayList<>());
            eventsCachingService.saveEvent(e.id(), e);

            //retrieve the attachments for event
            List<UploadedAttachment> attachments = eventsAttachmentsService.getAttachmentsOfEvent(event.getId());
            EventsWithAttachments updatedE = new EventsWithAttachments(
                e.id(),
                e.type(),
                e.name(),
                e.description(),
                e.location(),
                e.startDate(),
                e.endDate(),
                e.organizer(),
                attachments,
                e.metadata(),
                e.updatedAt(),
                e.createdAt()
            );
            eventsWithAttachments.add(updatedE);

            //store in cache
            eventsCachingService.saveEvent(e.id(), e);
        }
        return eventsWithAttachments;
    }

    @Transactional
    public EventsWithAttachments updateEvent(UUID id, EventsUpdate dto) {
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

        EventsWithAttachments updatedEventWithAttachments = EventsMapper.toEventsWithAttachments(
            updatedEvent
            , eventsAttachmentsService.getAttachmentsOfEvent(updatedEvent.getId())
        );

        //save to cache
        eventsCachingService.saveEvent(updatedEventWithAttachments.id(), updatedEventWithAttachments);

        return updatedEventWithAttachments;
    }

    @Transactional
    public int deleteEvent(UUID id) {
        int result = eventsRepository.deleteByIdSoft(id);
        if (result == 0) {
            throw new GlobalException(HttpStatus.NOT_FOUND, ErrorMessage.EVENT_NOT_FOUND);
        }

        //delete from cache
        eventsCachingService.deleteEvent(id.toString());

        return result;
    }
}
