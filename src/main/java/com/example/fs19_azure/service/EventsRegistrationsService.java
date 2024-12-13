package com.example.fs19_azure.service;

import com.example.fs19_azure.dto.EventsRegistrationsMessage;
import com.example.fs19_azure.entity.Events;
import com.example.fs19_azure.entity.EventsRegistrations;
import com.example.fs19_azure.entity.EventsRegistrationsStatus;
import com.example.fs19_azure.exceptions.ErrorMessage;
import com.example.fs19_azure.exceptions.GlobalException;
import com.example.fs19_azure.repository.EventsRegistrationsRepository;
import com.example.fs19_azure.repository.EventsRepository;
import com.example.fs19_azure.service.azure.ServiceBusProducerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.Optional;
import java.util.UUID;

@Service
public class EventsRegistrationsService {
    @Autowired
    private EventsRegistrationsRepository eventsRegistrationsRepository;

    @Autowired
    private EventsRepository eventsRepository;

    @Autowired
    private ServiceBusProducerService serviceBusProducerService;

    public EventsRegistrations registerUser(UUID eventId, UUID userId) {
        //check if the event exists
        Events event = eventsRepository.findById(eventId).get();
        if (event == null) {
            throw new GlobalException(HttpStatus.NOT_FOUND, ErrorMessage.EVENT_NOT_FOUND);
        }

        //send the registration message to the service bus
        EventsRegistrationsMessage message = new EventsRegistrationsMessage(
            "00000000-0000-0000-0000-000000000000"
            , eventId.toString()
            , EventsRegistrationsStatus.PENDING.name()
            , Instant.now().toString()
        );
        serviceBusProducerService.sendMessage(message);

        //create the registration record, status is PENDING
        EventsRegistrations eventsRegistrations = new EventsRegistrations().builder()
            .event(event)
            .userId(userId)
            .status(EventsRegistrationsStatus.PENDING.name())
            .build();

        return eventsRegistrationsRepository.save(eventsRegistrations);
    }

    public boolean confirmRegistration(UUID eventId, UUID userId) {
        //TODO: Implement the logic to check if the user exists

        //TODO: Implement the logic to check if the event exists

        //check if the pending registration exists
        Optional<EventsRegistrations> eventsRegistrations = eventsRegistrationsRepository
            .findByEventIdAndUserIdAndStatusIs(
                eventId
                , userId
                , EventsRegistrationsStatus.PENDING.name());

        if (eventsRegistrations.isEmpty()) {
            return false;
        }

        //update the registration record, status is CONFIRMED
        eventsRegistrations.get().setStatus(EventsRegistrationsStatus.CONFIRMED.name());
        eventsRegistrationsRepository.save(eventsRegistrations.get());
        return true;
    }
}
