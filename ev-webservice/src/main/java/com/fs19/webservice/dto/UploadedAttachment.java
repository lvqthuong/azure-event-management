package com.fs19.webservice.dto;

public record UploadedAttachment(
    String id,
    String blobUrl,
    String blobName,
    String blobType,
    Long blobSize
) {
}
