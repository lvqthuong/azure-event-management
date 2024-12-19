package com.example.fs19_azure.service.redis;

import com.example.fs19_azure.dto.UploadedAttachment;
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

    private static final String ATTACHMENTS_KEY_PREFIX = "event:attachments:";

    public EventsAttachmentsRedisService(RedisTemplate<String, Object> redisTemplate) {
        this.redisTemplate = redisTemplate;
        this.hashOperations = redisTemplate.opsForHash();
    }

    //save attachments for an event
    public void saveAttachments(String eventId, List<UploadedAttachment> attachments) {
        //if the attachments is empty, do nothing
        if (attachments.size() == 0)
        {
            System.out.println("Empty list of attachments, do nothing!");
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
    }
}