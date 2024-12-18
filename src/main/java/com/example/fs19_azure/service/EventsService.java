package com.example.fs19_azure.service;

import com.example.fs19_azure.dto.*;
import com.example.fs19_azure.dto.mapper.EventsMapper;
import com.example.fs19_azure.entity.Events;
import com.example.fs19_azure.entity.Users;
import com.example.fs19_azure.exceptions.ErrorMessage;
import com.example.fs19_azure.exceptions.GlobalException;
import com.example.fs19_azure.repository.EventsRepository;
import com.example.fs19_azure.repository.UsersRepository;

import org.springframework.data.redis.core.RedisTemplate;
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
    private RedisTemplate<String, Object> redisTemplate;
    private static final String EVENT_CACHE_KEY_PREFIX = "event:";

    @Autowired
    private EventsRepository eventsRepository;

    @Autowired
    private UsersRepository usersRepository;

    @Autowired
    private EventsAttachmentsService eventsAttachmentsService;

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
        redisTemplate.opsForValue().set(
            EVENT_CACHE_KEY_PREFIX + event.getId()
            , eventsWithAttachments
        );

        logger.info("Event created: {}", event.getId());
        return eventsWithAttachments;
    }

    public EventsWithAttachments getEvent(UUID id) {

        EventsWithAttachments eventFromCache = (EventsWithAttachments) redisTemplate.opsForValue().get(
           EVENT_CACHE_KEY_PREFIX + id
        );

        //cache hit
        if (eventFromCache != null) {
            return eventFromCache;
        }

        //cache miss
        Optional<Events> event = eventsRepository.findById(id);
        if (event.isEmpty()) {
            throw new GlobalException(HttpStatus.NOT_FOUND, ErrorMessage.EVENT_NOT_FOUND);
        }

        List<UploadedAttachment> attachments = eventsAttachmentsService.getAttachmentsOfEvent(id);

        //store in cache
        eventFromCache = EventsMapper.toEventsWithAttachments(event.get(), attachments);
        redisTemplate.opsForValue().set(
            EVENT_CACHE_KEY_PREFIX + id
            , eventFromCache
        );

        //convert to DTO
        return eventFromCache;
    }

    public List<EventsWithAttachments> getAllActiveEvents() {
        //retrieve all events from cache
        Set<String> keys = redisTemplate.keys(EVENT_CACHE_KEY_PREFIX + "*");

        // Retrieve values for each key
        List<EventsWithAttachments> events = new ArrayList<>();
        if (keys.stream().count() > 0) {
            for (String key : keys) {
                EventsWithAttachments event = (EventsWithAttachments) redisTemplate.opsForValue().get(key);
                if (event != null) {
                    events.add(event);
                }
            }
            return events;
        }

        //if cache miss, fetch from database
        List<Events> list = eventsRepository.findByDeletedFalse();
        List<EventsWithAttachments> eventsWithAttachments = new ArrayList<>();

        for (Events event : list) {
            List<UploadedAttachment> attachments = eventsAttachmentsService.getAttachmentsOfEvent(event.getId());
            eventsWithAttachments.add(EventsMapper.toEventsWithAttachments(event, attachments));

            //store in cache
            redisTemplate.opsForValue().set(
                EVENT_CACHE_KEY_PREFIX + event.getId()
                , EventsMapper.toEventsWithAttachments(event, attachments)
            );
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

        //invalidate cache
        redisTemplate.opsForValue().set(
            EVENT_CACHE_KEY_PREFIX + updatedEvent.getId()
            , updatedEventWithAttachments
        );
        return updatedEventWithAttachments;
    }

    @Transactional
    public int deleteEvent(UUID id) {
        int result = eventsRepository.deleteByIdSoft(id);
        if (result == 0) {
            throw new GlobalException(HttpStatus.NOT_FOUND, ErrorMessage.EVENT_NOT_FOUND);
        }

        //delete from cache
        redisTemplate.delete(EVENT_CACHE_KEY_PREFIX + id);

        return result;
    }
}
