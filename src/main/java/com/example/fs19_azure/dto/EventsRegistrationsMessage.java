package com.example.fs19_azure.dto;

public record EventsRegistrationsMessage(
    String userId,
    String eventId,
    String status,
    String timestamp
) {
}
