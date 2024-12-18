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
@RequestMapping("/events/{id}")
public class EventsRegistrationsController {

    @Autowired
    private EventsRegistrationsService eventsRegistrationsService;

    @Autowired
    private ServiceBusProducerService serviceBusProducerService;

    @PostMapping("/register/{userId}")
    public ResponseEntity<GlobalResponse<EventsRegistrations>> register(
        @Valid @PathVariable UUID id
        , @Valid @PathVariable UUID userId
    ) {
        return new ResponseEntity<>(
            new GlobalResponse<>(
                HttpStatus.OK.value()
                , eventsRegistrationsService.registerUser(
                    id
                    , userId
                )
            )
            , HttpStatus.OK
        );
    }

    @PostMapping("/confirm/{userId}")
    public ResponseEntity<GlobalResponse<Boolean>> confirmRegistration(
        @Valid @PathVariable UUID id
        , @Valid @PathVariable UUID userId
    ) {
        return new ResponseEntity<>(
            new GlobalResponse<>(
                HttpStatus.OK.value()
                , eventsRegistrationsService.confirmRegistration(
                    id
                    , userId
                )
            )
            , HttpStatus.OK
        );
    }
}
