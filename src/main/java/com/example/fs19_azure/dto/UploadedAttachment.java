package com.example.fs19_azure.dto;

public record UploadedAttachment(
    String blobUrl,
    String blobName,
    String blobType,
    Long blobSize
) {
}
