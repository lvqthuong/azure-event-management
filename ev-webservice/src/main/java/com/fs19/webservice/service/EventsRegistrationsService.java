package com.fs19.webservice.service;

import com.fs19.webservice.dto.EventsRegistrationsMessage;
import com.fs19.webservice.entity.Events;
import com.fs19.webservice.entity.EventsRegistrations;
import com.fs19.webservice.entity.EventsRegistrationsStatus;
import com.fs19.webservice.exceptions.ErrorMessage;
import com.fs19.webservice.exceptions.GlobalException;
import com.fs19.webservice.repository.EventsRegistrationsRepository;
import com.fs19.webservice.repository.EventsRepository;
import com.fs19.webservice.service.azure.ServiceBusProducerService;
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
            userId.toString()
            , eventId.toString()
            , EventsRegistrationsStatus.PENDING.name()
            , Instant.now().toString()
        );
        serviceBusProducerService.sendMessage(message);

        //create the registration record, status is PENDING
        EventsRegistrations eventsRegistrations = new EventsRegistrations().builder()
            .eventId(eventId)
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
            .findByEventIdAndUserIdAndStatus(
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
