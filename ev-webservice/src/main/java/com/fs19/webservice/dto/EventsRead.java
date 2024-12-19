package com.fs19.webservice.dto;

public record EventsRead (
    String id,
    String type,
    String name,
    String description,
    String location,
    String startDate,
    String endDate,
    String organizer,
    String metadata,
    String updatedAt,
    String createdAt
) {
}
