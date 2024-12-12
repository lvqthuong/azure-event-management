package com.example.fs19_azure.service;

import com.azure.messaging.servicebus.*;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;

@Service
public class ServiceBusConsumerService {

    private ServiceBusProcessorClient processorClient;

    public ServiceBusConsumerService (
        @Value("${SERVICE_BUS_CONNECTION_STRING}") String serviceBusConnectionString,
        @Value("${SERVICE_BUS_QUEUE_NAME}") String serviceBusQueueName
    ) {
//        System.out.println("ServiceBusConsumerService constructor called with connection string: " + serviceBusConnectionString + " and queue name: " + serviceBusQueueName);
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
        ServiceBusReceivedMessage message = context.getMessage();
        System.out.printf("Received message: %s%n", message.getBody().toString());

        // Mark the message as completed
        context.complete();
    }

    private void processError(ServiceBusErrorContext context) {
        System.err.printf("Error occurred while processing message: %s%n", context.getException().getMessage());
    }
}
