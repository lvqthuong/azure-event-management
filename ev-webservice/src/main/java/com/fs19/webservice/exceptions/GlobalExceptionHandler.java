package com.fs19.webservice.exceptions;

import com.fs19.webservice.controller.response.GlobalResponse;

import com.fasterxml.jackson.core.JsonProcessingException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.util.List;
import java.util.NoSuchElementException;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<GlobalResponse> handleValidationExceptions(MethodArgumentNotValidException ex) {

        List<GlobalResponse.ErrorItem> errors = ex.getBindingResult().getFieldErrors().stream()
            .map(err -> new GlobalResponse.ErrorItem(err.getField() + " " + err.getDefaultMessage()))
            .toList();

        return new ResponseEntity<>(
            new GlobalResponse(
                HttpStatus.BAD_REQUEST.value()
                , errors
            )
            , null
            , HttpStatus.BAD_REQUEST
        );
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<GlobalResponse> handleValidationExceptions(HttpMessageNotReadableException ex) {

        List<GlobalResponse.ErrorItem> errors = List.of(new GlobalResponse.ErrorItem(ErrorMessage.INVALID_JSON.getMessage()));
        return new ResponseEntity<>(
            new GlobalResponse(HttpStatus.BAD_REQUEST.value(), errors)
            , null
            , HttpStatus.BAD_REQUEST
        );
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<GlobalResponse> handleIllegalArgumentException(IllegalArgumentException ex) {

        return new ResponseEntity<>(
            new GlobalResponse<>(
                HttpStatus.BAD_REQUEST.value()
                , List.of(new GlobalResponse.ErrorItem(ex.getMessage()))
            ),
            HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(NoSuchElementException.class)
    public ResponseEntity<GlobalResponse> handleNoSuchElementException(NoSuchElementException ex) {
        return new ResponseEntity<>(
            new GlobalResponse<>(
                HttpStatus.NOT_FOUND.value()
                , List.of(new GlobalResponse.ErrorItem("Item not found"))
            )
            , HttpStatus.NOT_FOUND
        );
    }

    @ExceptionHandler(JsonProcessingException.class)
    public ResponseEntity<GlobalResponse> handleJsonProcessingException(JsonProcessingException ex) {
        return new ResponseEntity<>(
            new GlobalResponse<>(
                HttpStatus.BAD_REQUEST.value()
                , List.of(new GlobalResponse.ErrorItem(ErrorMessage.INVALID_JSON.getMessage()))
            )
            , HttpStatus.NOT_FOUND
        );
    }

    @ExceptionHandler(SendMessageProcessingException.class)
    public ResponseEntity<GlobalResponse> handleSendMessageProcessingException(SendMessageProcessingException ex) {
        return new ResponseEntity<>(
            new GlobalResponse<>(
                HttpStatus.INTERNAL_SERVER_ERROR.value()
                , List.of(new GlobalResponse.ErrorItem(ErrorMessage.MAPPING_SEND_MESSAGE_ERROR.getMessage()))
            )
            , HttpStatus.INTERNAL_SERVER_ERROR
        );
    }

    @ExceptionHandler(FileUploadException.class)
    public ResponseEntity<GlobalResponse> handleFileUploadException(FileUploadException ex) {
        return new ResponseEntity<>(
            new GlobalResponse<>(
                HttpStatus.INTERNAL_SERVER_ERROR.value()
                , List.of(new GlobalResponse.ErrorItem(ErrorMessage.FILE_UPLOAD_FAILED.getMessage()))
            )
            , HttpStatus.INTERNAL_SERVER_ERROR
        );
    }

    @ExceptionHandler(BlobStorageException.class)
    public ResponseEntity<GlobalResponse> handleBlobStorageException(BlobStorageException ex) {
        return new ResponseEntity<>(
            new GlobalResponse<>(
                ex.getStatusCode()
                , List.of(ex.getMessage())
            )
            , HttpStatus.INTERNAL_SERVER_ERROR
        );
    }

    @ExceptionHandler(GlobalException.class)
    public ResponseEntity<GlobalResponse> handleApplicationException(GlobalException ex){
        List<GlobalResponse.ErrorItem> errorItems = List.of(new GlobalResponse.ErrorItem(ex.getMessage()));
        return new ResponseEntity<>(
            new GlobalResponse(
                ex.getStatus().value()
                , errorItems
            )
            ,null
            , ex.getStatus()
        );
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<String> handleGenericException(Exception ex) {
        return new ResponseEntity<>("An error occurred: " + ex.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
