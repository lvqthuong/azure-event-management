package com.fs19.webservice.dto;

public record EventsRegistrationsMessage(
    String userId,
    String eventId,
    String status,
    String timestamp
) {
}
