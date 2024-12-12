package com.example.fs19_azure.controller;

import com.example.fs19_azure.controller.response.GlobalResponse;
import com.example.fs19_azure.dto.UploadedAttachment;
import com.example.fs19_azure.entity.EventsAttachments;
import com.example.fs19_azure.service.EventsAttachmentsService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.Valid;
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

    @Operation(summary = "Upload a file", description = "Upload a file to the server")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "File uploaded successfully"),
        @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @PostMapping
    public ResponseEntity<GlobalResponse<List<UploadedAttachment>>> uploadAttachments(
        @PathVariable UUID id
        , @RequestParam("file") List<MultipartFile> attachments
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
}
