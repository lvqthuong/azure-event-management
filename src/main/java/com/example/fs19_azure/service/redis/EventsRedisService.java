package com.example.fs19_azure.service.redis;

import com.example.fs19_azure.dto.EventsWithAttachments;
import com.example.fs19_azure.exceptions.GlobalException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.data.redis.core.Cursor;
import org.springframework.data.redis.core.RedisCallback;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ScanOptions;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Service
public class EventsRedisService {
    private final RedisTemplate<String, Object> redisTemplate;

    private static final String EVENT_CACHE_KEY_PREFIX = "events:";

    public EventsRedisService(RedisTemplate<String, Object> redisTemplate) {
        this.redisTemplate = redisTemplate;
    }

    //get single event
    public EventsWithAttachments getEvent(String eventId){
        String key = EVENT_CACHE_KEY_PREFIX + eventId;
        String cachedEvent = (String) redisTemplate.opsForValue().get(key);

        try {
            return new ObjectMapper().readValue(cachedEvent, EventsWithAttachments.class);
        } catch (IOException e) {
            throw new GlobalException("Failed to parse cached event for ID: " + eventId);
        }
    }

    //save event
    public void saveEvent(String eventId, EventsWithAttachments event) {
        String key = EVENT_CACHE_KEY_PREFIX + eventId;

        try {
            String ec = new ObjectMapper().writeValueAsString(event);
            redisTemplate.opsForValue().set(key, ec);
        } catch (JsonProcessingException e) {
            throw new GlobalException("Failed to write event as Json for ID: " + eventId);
        }
    }

    //get all events for an event
    public List<EventsWithAttachments> getAllEvents() {
        String keyPattern = EVENT_CACHE_KEY_PREFIX + "*";
        List<EventsWithAttachments> events = new ArrayList<>();

        ScanOptions scanOptions = ScanOptions.scanOptions().match(keyPattern).build();

        Cursor<byte[]> cursor = (Cursor<byte[]>) redisTemplate
            .execute((RedisCallback<Cursor<byte[]>>) connection ->
                connection.scan(scanOptions)
            );

        while (cursor != null && cursor.hasNext()) {
            String currentKey = new String(cursor.next());
            String cachedEvent = (String) redisTemplate.opsForValue().get(currentKey);
            try {
                EventsWithAttachments event = new ObjectMapper().readValue(cachedEvent, EventsWithAttachments.class);
                events.add(event);
            } catch (IOException e) {
                throw new GlobalException("Failed to parse cached event for ID: " + currentKey);
            }
        }

        return events;
    }

    //remove an event from cache
    public void deleteEvent(String eventId) {
        String key = EVENT_CACHE_KEY_PREFIX + eventId;
        redisTemplate.delete(key);
    }
}
