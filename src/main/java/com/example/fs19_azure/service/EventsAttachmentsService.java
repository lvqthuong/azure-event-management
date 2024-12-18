package com.example.fs19_azure.service;

import com.example.fs19_azure.dto.UploadedAttachment;
import com.example.fs19_azure.entity.EventsAttachments;
import com.example.fs19_azure.exceptions.FileUploadException;
import com.example.fs19_azure.repository.EventsAttachmentsRepository;
import com.example.fs19_azure.service.azure.BlobStorageService;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import com.azure.storage.blob.BlobClientBuilder;
import com.azure.storage.blob.BlobContainerClient;
import com.azure.storage.blob.BlobContainerClientBuilder;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@Service
public class EventsAttachmentsService {
    @Autowired
    private EventsAttachmentsRepository eventsAttachmentsRepository;

    @Autowired
    private BlobStorageService blobStorageService;

    private final int MAX_FILE_SIZE = 1024*1024*2; // 2MB

    public List<UploadedAttachment> getAttachmentsOfEvent(UUID eventId) {
        List<EventsAttachments> attachments = eventsAttachmentsRepository.findByEventId(eventId);
        List<UploadedAttachment> uploadedAttachments = new ArrayList<>();
        for (EventsAttachments attachment : attachments) {
            uploadedAttachments.add(new UploadedAttachment(
                attachment.getId().toString()
                , attachment.getBlob_url()
                , attachment.getBlob_name()
                , attachment.getBlob_type()
                , attachment.getBlob_size()
            ));
        }
        return uploadedAttachments;
    }

    public List<UploadedAttachment> uploadEventAttachments(UUID eventId, List<MultipartFile> files) {
        List<UploadedAttachment> uploadedAttachments = new ArrayList<>();
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
}
