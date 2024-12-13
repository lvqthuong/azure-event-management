package com.example.fs19_azure.controller;

import com.example.fs19_azure.controller.response.GlobalResponse;
import com.example.fs19_azure.entity.EventsRegistrations;
import com.example.fs19_azure.service.EventsRegistrationsService;
import com.example.fs19_azure.service.azure.ServiceBusProducerService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

@Validated
@RestController
@RequestMapping("/events/{id}/register")
public class EventsRegistrationsController {

    @Autowired
    private EventsRegistrationsService eventsRegistrationsService;

    @Autowired
    private ServiceBusProducerService serviceBusProducerService;

    @PostMapping
    public ResponseEntity<GlobalResponse<EventsRegistrations>> register(@Valid @PathVariable UUID id) {
        return new ResponseEntity<>(
            new GlobalResponse<>(
                HttpStatus.OK.value()
                , eventsRegistrationsService.registerUser(
                    id
                    , UUID.fromString("00000000-0000-0000-0000-000000000000")
                )
            )
            , HttpStatus.OK
        );
    }

}
