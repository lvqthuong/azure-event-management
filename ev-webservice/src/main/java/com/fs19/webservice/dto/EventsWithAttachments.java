package com.fs19.webservice.dto;

import java.util.List;

public record EventsWithAttachments(
    String id,
    String type,
    String name,
    String description,
    String location,
    String startDate,
    String endDate,
    String organizer,
    List<UploadedAttachment> attachments,
    String metadata,
    String updatedAt,
    String createdAt
) {
}
