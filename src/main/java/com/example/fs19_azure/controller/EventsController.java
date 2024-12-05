package com.example.fs19_azure.controller;

import com.example.fs19_azure.controller.response.GlobalResponse;
import com.example.fs19_azure.dto.EventsCreate;
import com.example.fs19_azure.entity.Events;
import com.example.fs19_azure.service.EventsService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/events")
@Validated
public class EventsController {
    @Autowired
    private EventsService eventsService;

    @PostMapping
    public ResponseEntity<GlobalResponse<Events>> createEvent(@RequestBody @Valid EventsCreate dto) {
        return new ResponseEntity<>(
            new GlobalResponse<>(
                HttpStatus.CREATED.value()
                , eventsService.createEvent(dto)
            )
            , HttpStatus.CREATED
        );
    }

    @GetMapping("/{id}")
    public ResponseEntity<GlobalResponse<Events>> getEvent(@PathVariable UUID id) {
        return new ResponseEntity<>(
            new GlobalResponse<>(
                HttpStatus.OK.value()
                , eventsService.getEvent(id)
            )
            , HttpStatus.OK
        );
    }

    @GetMapping
    public ResponseEntity<GlobalResponse<List<Events>>> getEvents() {
        return new ResponseEntity<>(
            new GlobalResponse<>(
                HttpStatus.OK.value()
                , eventsService.getAllEvents()
            )
            , HttpStatus.OK
        );
    }
}
