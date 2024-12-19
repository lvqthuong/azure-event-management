package com.fs19.webservice.controller;

import com.fs19.webservice.controller.response.GlobalResponse;
import com.fs19.webservice.dto.EventsRegistrationsMessage;
import com.fs19.webservice.entity.Events;
import com.fs19.webservice.entity.EventsRegistrationsStatus;
import com.fs19.webservice.exceptions.ErrorMessage;
import com.fs19.webservice.exceptions.GlobalException;
import com.fs19.webservice.repository.EventsRepository;
import com.fs19.webservice.service.ServiceBusClientService;
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
