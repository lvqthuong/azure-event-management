package com.example.fs19_azure.controller;

import com.example.fs19_azure.controller.response.GlobalResponse;
import com.example.fs19_azure.dto.UploadedAttachment;
import com.example.fs19_azure.service.EventsAttachmentsService;
import io.swagger.v3.oas.annotations.Parameter;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.UUID;

@Validated
@RequestMapping("/events/{id}/attachments")
@RestController
public class EventsAttachmentsController {
    @Autowired
    private EventsAttachmentsService eventsAttachmentsService;

    /*
        curl -X POST \
            -F "file=@resource-init.sh" \
            http://localhost:8080/events/7ff6a1ff-61f1-4c31-a8e9-a3e722b8b80b/attachments
     */
    @PostMapping(value = "/upload", consumes = "multipart/form-data")
    public ResponseEntity<GlobalResponse<List<UploadedAttachment>>> uploadAttachments(
        @PathVariable UUID id
        , @Parameter(
            description = "List of files to upload"
            , required = true
        )
        @RequestParam("file") List<MultipartFile> attachments
    ) {
        System.out.println("uploadAttachments for event id: " + id);
        return new ResponseEntity<>(
            new GlobalResponse<>(
                HttpStatus.CREATED.value(),
                eventsAttachmentsService.uploadEventAttachments(id, attachments)
            )
            , HttpStatus.CREATED
        );
    }

    @DeleteMapping("/{attachmentId}")
    public ResponseEntity<GlobalResponse<Boolean>> deleteAttachment(
        @PathVariable UUID id
        , @PathVariable UUID attachmentId
    ) {
        return new ResponseEntity<>(
            new GlobalResponse<>(
                HttpStatus.OK.value()
                , eventsAttachmentsService.deleteEventAttachment(id, attachmentId)
            )
            , HttpStatus.OK
        );
    }
}
