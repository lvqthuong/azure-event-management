package com.example.fs19_azure.service.azure;

import com.azure.storage.blob.BlobContainerClient;
import com.azure.storage.blob.BlobContainerClientBuilder;
import com.example.fs19_azure.dto.UploadedAttachment;
import com.example.fs19_azure.exceptions.FileUploadException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.UUID;

@Service
public class BlobStorageService {

    private static final Logger logger = LoggerFactory.getLogger(BlobStorageService.class);
    private final BlobContainerClient blobContainerClient;

    public BlobStorageService (
        @Value("${BLOB_STORAGE_CONNECTION_STRING}") String connectionString
        , @Value("${BLOB_STORAGE_CONTAINER_NAME}") String containerName
    ) {

        this.blobContainerClient = new BlobContainerClientBuilder()
            .connectionString(connectionString)
            .containerName(containerName)
            .buildClient();
    }

    public UploadedAttachment uploadFileForEvent(String eventId, MultipartFile file) {
        try {
            UUID uuid = UUID.randomUUID();
            String fileName = eventId + "_" + uuid + "_" + file.getOriginalFilename();
            blobContainerClient.getBlobClient(fileName)
                .upload(file.getInputStream(), file.getSize(), true);

            String fileUrl = blobContainerClient.getBlobClient(fileName).getBlobUrl();
            return new UploadedAttachment(
                uuid.toString()
                , fileUrl
                , fileName
                , file.getContentType()
                , file.getSize()
            );

        } catch (IOException e) {
            logger.error("Failed to upload file to blob storage.", e);
            throw new FileUploadException("Failed to upload file to blob storage.", e);
        }
    }
}
