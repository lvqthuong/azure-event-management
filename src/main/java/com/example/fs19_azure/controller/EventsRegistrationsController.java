package com.example.fs19_azure.controller;

import com.example.fs19_azure.controller.response.GlobalResponse;
import com.example.fs19_azure.dto.EventsRegistrationsMessage;
import com.example.fs19_azure.entity.Events;
import com.example.fs19_azure.entity.EventsRegistrationsStatus;
import com.example.fs19_azure.exceptions.ErrorMessage;
import com.example.fs19_azure.exceptions.GlobalException;
import com.example.fs19_azure.repository.EventsRepository;
import com.example.fs19_azure.service.ServiceBusProducerService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.Instant;
import java.util.UUID;

@Validated
@RestController
@RequestMapping("/events/{id}/register")
public class EventsRegistrationsController {

    @Autowired
    private EventsRepository eventsRepository;

    @Autowired
    private ServiceBusProducerService serviceBusProducerService;

    @PostMapping
    public ResponseEntity<GlobalResponse<String>> register(@Valid @PathVariable UUID id) throws Exception {
        //check if the event exists
        Events event = eventsRepository.findById(id).get();
        if (event == null) {
            throw new GlobalException(HttpStatus.NOT_FOUND, ErrorMessage.EVENT_NOT_FOUND);
        }

        //TODO: Implement the logic to check if the user is already registered for the event

        //send the registration message to the service bus
        EventsRegistrationsMessage message = new EventsRegistrationsMessage(
            "00000000-0000-0000-0000-000000000000"
            , id.toString()
            , EventsRegistrationsStatus.REGISTERED.name()
            , Instant.now().toString()
        );
        serviceBusProducerService.sendMessage(message);

        return new ResponseEntity<>(
            new GlobalResponse<>(
                HttpStatus.OK.value(), "Registration successful"
            )
            , HttpStatus.OK
        );
    }

}
