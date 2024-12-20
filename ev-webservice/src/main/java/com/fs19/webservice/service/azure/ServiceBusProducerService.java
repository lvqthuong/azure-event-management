package com.fs19.webservice.service.azure;

import com.azure.messaging.servicebus.*;
import com.fs19.webservice.dto.EventsRegistrationsMessage;
import com.fs19.webservice.exceptions.SendMessageProcessingException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class ServiceBusProducerService {

    private static final Logger logger = LoggerFactory.getLogger(ServiceBusProducerService.class);
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
            senderClient.sendMessage(new ServiceBusMessage(message).setMessageId(UUID.randomUUID().toString()));
        } catch (JsonProcessingException e) {
            logger.error("Failed to create JSON message to send to Service Bus", e);
            throw new SendMessageProcessingException("Failed to create message JSON", e);
        }
    }
}
