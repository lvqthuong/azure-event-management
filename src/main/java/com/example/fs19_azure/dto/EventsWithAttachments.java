package com.example.fs19_azure.dto;

import com.example.fs19_azure.entity.EventsAttachments;

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
    List<EventsAttachments> attachments,
    String metadata,
    String updatedAt,
    String createdAt
) {
}
