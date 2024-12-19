package com.fs19.webservice.controller;

import com.fs19.webservice.controller.response.GlobalResponse;
import com.fs19.webservice.dto.EventsCreate;
import com.fs19.webservice.dto.EventsRead;
import com.fs19.webservice.dto.EventsUpdate;
import com.fs19.webservice.entity.Events;
import com.fs19.webservice.service.EventsService;
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

    @PostMapping("/create")
    public ResponseEntity<GlobalResponse<EventsWithAttachments>> createEvent(@RequestBody @Valid EventsCreate dto) {
        return new ResponseEntity<>(
            new GlobalResponse<>(
                HttpStatus.CREATED.value()
                , eventsService.createEvent(dto)
            )
            , HttpStatus.CREATED
        );
    }

    @GetMapping("/{id}")
    public ResponseEntity<GlobalResponse<EventsWithAttachments>> getEvent(@PathVariable UUID id) {
        return new ResponseEntity<>(
            new GlobalResponse<>(
                HttpStatus.OK.value()
                , eventsService.getEvent(id)
            )
            , HttpStatus.OK
        );
    }

    @GetMapping
    public ResponseEntity<GlobalResponse<List<EventsWithAttachments>>> getActiveEvents() {
        return new ResponseEntity<>(
            new GlobalResponse<>(
                HttpStatus.OK.value()
                , eventsService.getAllActiveEvents()
            )
            , HttpStatus.OK
        );
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<GlobalResponse<EventsWithAttachments>> updateEvent(@PathVariable UUID id, @RequestBody @Valid EventsUpdate dto) {
        return new ResponseEntity<>(
            new GlobalResponse<>(
                HttpStatus.OK.value()
                , eventsService.updateEvent(id, dto)
            )
            , HttpStatus.OK
        );
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<GlobalResponse<String>> deleteEvent(@PathVariable UUID id) {
        int result = eventsService.deleteEvent(id);
        return new ResponseEntity<>(
            new GlobalResponse<>(
                HttpStatus.OK.value()
                , "Event deleted"
            )
            , HttpStatus.OK
        );
    }
}
