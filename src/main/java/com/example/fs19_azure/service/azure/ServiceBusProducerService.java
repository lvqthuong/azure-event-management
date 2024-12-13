package com.example.fs19_azure.service.azure;

import com.azure.messaging.servicebus.*;
import com.example.fs19_azure.dto.EventsRegistrationsMessage;
import com.example.fs19_azure.exceptions.SendMessageProcessingException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class ServiceBusProducerService {

    private final ServiceBusSenderClient senderClient;

    public ServiceBusProducerService(
        @Value("${SERVICE_BUS_CONNECTION_STRING}") String serviceBusConnectionString,
        @Value("${SERVICE_BUS_QUEUE_NAME}") String serviceBusQueueName
    ) {
        this.senderClient = new ServiceBusClientBuilder()
            .connectionString(serviceBusConnectionString)
            .sender()
            .queueName(serviceBusQueueName)
            .buildClient();;
    }

    public void sendMessage(EventsRegistrationsMessage erMessage) {
        try {
            String message = new ObjectMapper().writeValueAsString(erMessage);
            System.out.println("Sending message: " + message);
            senderClient.sendMessage(new ServiceBusMessage(message).setMessageId(UUID.randomUUID().toString()));
        } catch (JsonProcessingException e) {
            throw new SendMessageProcessingException("Failed to create message JSON", e);
        }
    }
}
