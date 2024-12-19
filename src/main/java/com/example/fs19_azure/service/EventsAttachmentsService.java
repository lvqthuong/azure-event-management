package com.example.fs19_azure.service;

import com.example.fs19_azure.dto.UploadedAttachment;
import com.example.fs19_azure.entity.EventsAttachments;
import com.example.fs19_azure.exceptions.AttachmentNotFoundException;
import com.example.fs19_azure.repository.EventsAttachmentsRepository;
import com.example.fs19_azure.service.azure.BlobStorageService;
import com.example.fs19_azure.service.redis.EventsAttachmentsRedisService;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.*;

@Service
public class EventsAttachmentsService {

    @Autowired
    private EventsAttachmentsRedisService attachmentCachingService;

    @Autowired
    private EventsAttachmentsRepository eventsAttachmentsRepository;

    @Autowired
    private BlobStorageService blobStorageService;

    private final int MAX_FILE_SIZE = 1024*1024*2; // 2MB

    public List<UploadedAttachment> getAttachmentsOfEvent(UUID eventId) {
        //get the attachments from cache.deleteAttachment(eventId.toString(), attachmentId.toString());
        List<UploadedAttachment> attachments = attachmentCachingService.getAttachmentsForEvent(eventId.toString());

        //cache hit -> return
        if (attachments.size() > 0) {
            System.out.println("Attachment cache hit");
            return attachments;
        }

        //cache miss
        System.out.println("Attachment cache miss");
        // -> get the attachments from db
        List<EventsAttachments> rawAttachments = eventsAttachmentsRepository.findByEventId(eventId);
        List<UploadedAttachment> uploadedAttachments = new ArrayList<>();
        for (EventsAttachments attachment : rawAttachments) {
            uploadedAttachments.add(new UploadedAttachment(
                attachment.getId().toString()
                , attachment.getBlob_url()
                , attachment.getBlob_name()
                , attachment.getBlob_type()
                , attachment.getBlob_size()
            ));
        }

        // -> store the attachments to cache.deleteAttachment(eventId.toString(), attachmentId.toString());
        attachmentCachingService.saveAttachments(eventId.toString(), uploadedAttachments);

        return uploadedAttachments;
    }

    public List<UploadedAttachment> uploadEventAttachments(UUID eventId, List<MultipartFile> files) {
        List<UploadedAttachment> uploadedAttachments = new ArrayList<>();

        //uploading the files to Storage Service
        for (MultipartFile file : files) {

            //TODO: Implement the logic to check if the file size is less than 2MB

            // Upload the file to the blob storage
            UploadedAttachment uploadedFile = blobStorageService.uploadFileForEvent(eventId.toString(), file);

            // Save the file metadata to the database
            EventsAttachments attachment = eventsAttachmentsRepository.save(EventsAttachments.builder()
                .eventId(eventId)
                .blob_name(uploadedFile.blobName())
                .blob_type(uploadedFile.blobType())
                .blob_url(uploadedFile.blobUrl())
                .blob_size(uploadedFile.blobSize())
                .build());

            UploadedAttachment uploadFileWithId = new UploadedAttachment(
                attachment.getId().toString()
                , uploadedFile.blobUrl()
                , uploadedFile.blobName()
                , uploadedFile.blobType()
                , uploadedFile.blobSize()
            );

            uploadedAttachments.add(uploadFileWithId);
        }

        //store the metadata to cache.deleteAttachment(eventId.toString(), attachmentId.toString());
        attachmentCachingService.saveAttachments(eventId.toString(), uploadedAttachments);

        return uploadedAttachments;
    }

    @Transactional
    public void saveAttachmentsToEvent(UUID eventId, List<Map<String, Object>> assets) {
        for (Map<String, Object> asset : assets) {
            EventsAttachments attachment = EventsAttachments.builder()
                .eventId(eventId)
                .blob_name((String) asset.get("name"))
                .blob_type((String) asset.get("type"))
                .blob_url((String) asset.get("url"))
                .blob_size((Long) asset.get("size"))
                .build();
            eventsAttachmentsRepository.save(attachment);
        }
    }

    public boolean deleteEventAttachment(UUID eventId, UUID attachmentId) {

        Optional<EventsAttachments> attachment = eventsAttachmentsRepository.findByEventIdAndId(eventId, attachmentId);
        if (attachment.isEmpty()) {
            throw new AttachmentNotFoundException(
                "Attachment not found with id: " + attachmentId + " for event: " + eventId
                , null
            );
        }

        // Delete the file from the blob storage
        boolean result = blobStorageService.deleteFile(attachment.get().getBlob_name());

        if (result) {
            // Delete the file metadata from the database
            eventsAttachmentsRepository.deleteById(attachmentId);

            // delete from cache
            attachmentCachingService.deleteAttachment(eventId.toString(), attachmentId.toString());
        }

        return result;
    }
}
