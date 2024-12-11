package com.example.fs19_azure.controller;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.azure.messaging.servicebus.*;

@RestController
@RequestMapping("/events/register")
public class EventsRegistrationsController {

    @Value("${SERVICE_BUS_CONNECTION_STRING}")
    private String SERVICE_BUS_CONNECTION_STRING;

    @Value("${SERVICE_BUS_QUEUE_NAME}")
    private String SERVICE_BUS_QUEUE_NAME;


    @PostMapping
    public String register() {
        ServiceBusSenderClient sender = new ServiceBusClientBuilder()
            .connectionString(SERVICE_BUS_CONNECTION_STRING)
            .sender()
            .queueName(SERVICE_BUS_QUEUE_NAME)
            .buildClient();


        // Send one message to the queue
        String messageContent = "Event Registration Message";
        sender.sendMessage(new ServiceBusMessage(messageContent).setMessageId("1"));

        return "Registered";
    }

}
