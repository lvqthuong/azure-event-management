package com.fs19.webservice.service.redis;


import com.fs19.webservice.dto.EventsWithAttachments;
import com.fs19.webservice.dto.UploadedAttachment;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class EventsAttachmentsRedisService {
    private final RedisTemplate<String, Object> redisTemplate;
    private final HashOperations<String, String, UploadedAttachment> hashOperations;

    private static final String ATTACHMENTS_KEY_PREFIX = "attachments:";

    @Autowired
    private EventsRedisService eventsCachingService;

    public EventsAttachmentsRedisService(RedisTemplate<String, Object> redisTemplate) {
        this.redisTemplate = redisTemplate;
        this.hashOperations = redisTemplate.opsForHash();
    }

    //save attachments for an event
    public void saveAttachments(String eventId, List<UploadedAttachment> attachments) {
        //if the attachments is empty, do nothing
        if (attachments.size() == 0)
        {
            return;
        }

        System.out.println("Store " + attachments.stream().count() + " attachment(s) to cache for event:" + eventId);
        String key = ATTACHMENTS_KEY_PREFIX + eventId;

        //use BULK update instead of LOOP
        Map<String, UploadedAttachment> attachmentsMap = attachments
            .stream().collect(
                Collectors.toMap(UploadedAttachment::id, a -> a)
            );
        System.out.println("Serialized map: " + attachmentsMap);
        hashOperations.putAll(key, attachmentsMap);

        //update the event cache with the new event
        refreshEvent(eventId);
    }

    //get all attachments for an event
    public List<UploadedAttachment> getAttachmentsForEvent(String eventId) {
        String key = ATTACHMENTS_KEY_PREFIX + eventId;
        Map<String, UploadedAttachment> attachmentsMap = hashOperations.entries(key);
        return attachmentsMap.values().stream().toList();
    }

    //delete an attachment for an event
    public void deleteAttachment(String eventId, String attachmentId) {
        String key = ATTACHMENTS_KEY_PREFIX + eventId;
        hashOperations.delete(key, attachmentId);
        refreshEvent(eventId);
    }

    //refresh the event cache for new attachments
    private void refreshEvent(String eventId) {
        //refresh the event cache with the new event
        EventsWithAttachments event = eventsCachingService.getEvent(eventId);
        List<UploadedAttachment> updatedAttachments = getAttachmentsForEvent(eventId);
        System.out.println("List of attachment has " + updatedAttachments.size() + " items!");

        EventsWithAttachments updatedEvent = new EventsWithAttachments(
            event.id(),
            event.type(),
            event.name(),
            event.description(),
            event.location(),
            event.startDate(),
            event.endDate(),
            event.organizer(),
            updatedAttachments,
            event.metadata(),
            event.updatedAt(),
            event.createdAt()
        );

        eventsCachingService.saveEvent(eventId, updatedEvent);
    }
}