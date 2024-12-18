package com.example.fs19_azure.dto;

public record UploadedAttachment(
    String id,
    String blobUrl,
    String blobName,
    String blobType,
    Long blobSize
) {
}
