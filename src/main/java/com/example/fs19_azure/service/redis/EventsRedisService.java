package com.example.fs19_azure.service.redis;

import com.example.fs19_azure.dto.EventsWithAttachments;
import com.example.fs19_azure.dto.UploadedAttachment;
import com.example.fs19_azure.service.EventsAttachmentsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.Cursor;
import org.springframework.data.redis.core.RedisCallback;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ScanOptions;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class EventsRedisService {
    private final RedisTemplate<String, Object> redisTemplate;

    private static final String EVENT_CACHE_KEY_PREFIX = "event:";

    @Autowired
    private EventsAttachmentsRedisService attachmentsCachingService;

    public EventsRedisService(RedisTemplate<String, Object> redisTemplate) {
        this.redisTemplate = redisTemplate;
    }

    //get single event
    public EventsWithAttachments getEvent(String eventId) {
        String key = EVENT_CACHE_KEY_PREFIX + eventId;
        return (EventsWithAttachments) redisTemplate.opsForValue().get(key);
    }

    //save event
    public void saveEvent(String eventId, EventsWithAttachments event) {
        String key = EVENT_CACHE_KEY_PREFIX + eventId;
        redisTemplate.opsForValue().set(key, event);
    }

    //get all events for an event
    public List<EventsWithAttachments> getAllEvents() {
        String keyPattern = EVENT_CACHE_KEY_PREFIX;
        List<EventsWithAttachments> events = new ArrayList<>();

        ScanOptions scanOptions = ScanOptions.scanOptions().match(keyPattern).build();

        Cursor<byte[]> cursor = (Cursor<byte[]>) redisTemplate
            .execute((RedisCallback<Cursor<byte[]>>) connection ->
                connection.scan(scanOptions)
            );

        while (cursor != null && cursor.hasNext()) {
            String currentKey = new String(cursor.next());
            EventsWithAttachments event = (EventsWithAttachments) redisTemplate.opsForValue().get(currentKey);

            //get attachments for event
            List<UploadedAttachment> attachments = attachmentsCachingService
                .getAttachmentsForEvent(currentKey.split(":")[1]);

            EventsWithAttachments eventsWithAttachments = new EventsWithAttachments(
                event.id(),
                event.type(),
                event.name(),
                event.description(),
                event.location(),
                event.startDate(),
                event.endDate(),
                event.organizer(),
                attachments,
                event.metadata(),
                event.updatedAt(),
                event.createdAt()
            );

            events.add(event);
        }

        return events;
    }

    //remove an event from cache
    public void deleteEvent(String eventId) {
        String key = EVENT_CACHE_KEY_PREFIX + eventId;
        redisTemplate.delete(key);
    }
}
