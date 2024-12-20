package com.fs19.webservice.service.azure;

import com.azure.messaging.servicebus.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fs19.webservice.dto.EventsRegistrationsMessage;
import com.fs19.webservice.service.EventsRegistrationsService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class ServiceBusConsumerService {
    //TODO: Port the ServiceBusConsumerService to Azure Functions

    private static final Logger logger = LoggerFactory.getLogger(ServiceBusConsumerService.class);

    @Autowired
    private EventsRegistrationsService eventsRegistrationsService;

    private ServiceBusProcessorClient processorClient;

    public ServiceBusConsumerService (
        @Value("${SERVICE_BUS_CONNECTION_STRING}") String serviceBusConnectionString,
        @Value("${SERVICE_BUS_QUEUE_NAME}") String serviceBusQueueName
    ) {
        this.processorClient = new ServiceBusClientBuilder()
            .connectionString(serviceBusConnectionString)
            .processor()
            .queueName(serviceBusQueueName)
            .processMessage(this::processMessage)
            .processError(this::processError)
            .buildProcessorClient();
    }

    public void startProcessing() {
        if (processorClient == null) {
            throw new IllegalStateException("Processor client is not initialized.");
        }

        this.processorClient.start();
        System.out.println("Started message processor for queue: " + this.processorClient.getQueueName());
    }

    private void processMessage(ServiceBusReceivedMessageContext context) {
//        ServiceBusReceivedMessage message = context.getMessage();
//        System.out.printf("Received message: %s%n", message.getBody().toString());
//
//        EventsRegistrationsMessage erMessage;
//        try {
//            erMessage = new ObjectMapper().readValue(message.getBody().toString(), EventsRegistrationsMessage.class);
//            Boolean result = eventsRegistrationsService.confirmRegistration(
//                UUID.fromString(erMessage.eventId())
//                , UUID.fromString(erMessage.userId())
//            );
//
//            // Mark the message as completed
//            if (result != false) {
//                System.out.println("Message processed successfully.");
//                context.complete();
//            } else {
//                logger.error("Error occurred confirming registration for user: {} and event: {}", erMessage.userId(), erMessage.eventId());
//                context.abandon();
//            }
//        } catch (JsonProcessingException e) {
//            logger.error("Error occurred while parsing JSON message: {}", e.getMessage());
//            context.abandon();
//        }
        context.abandon();
    }

    private void processError(ServiceBusErrorContext context) {
        logger.error("Error occurred while processing message: {}", context.getException().getMessage());
    }
}
