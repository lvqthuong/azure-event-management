package com.example.fs19_azure.utility;

import com.example.fs19_azure.entity.Events;
import com.example.fs19_azure.entity.Users;
import com.example.fs19_azure.repository.EventsRepository;
import com.example.fs19_azure.service.EventsService;
import lombok.extern.log4j.Log4j2;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;

import com.example.fs19_azure.repository.UsersRepository;

import java.time.Instant;
import java.util.UUID;

@Log4j2
@Configuration
@Component
public class DataInitializer {
    @Autowired
    private UsersRepository usersRepository;

    @Autowired
    private EventsRepository eventsRepository;

    private static final Logger logger = LoggerFactory.getLogger(DataInitializer.class);

    private UUID userId;

    @Bean
    CommandLineRunner initData() {
        return args -> {
            initUsers();
            initEvents();
        };
    }

    private void initUsers() {
        UUID newUserId = UUID.randomUUID();
        Users user = usersRepository.save(
            Users.builder()
                .email(newUserId + "-test@fs19java.com")
                .name("Test User-" + newUserId)
                .password("password")
                .build()
        );
        userId = usersRepository.findByEmail(newUserId + "-test@fs19java.com").getId();
        logger.info("User created: {}", userId);
    }

    private void initEvents() {
        Events event = eventsRepository.save(
            Events.builder()
                .name("Test Event")
                .type("music")
                .description("Test Event Description")
                .startDate(Instant.now())
                .endDate(Instant.now())
                .location("Test Location")
                .organizer(usersRepository.findById(userId).get())
                .metadata("{ \"key\": \"value\" }")
                .build());

        logger.info("Event created: {}", event.getId());
    }


//    @Bean
//    CommandLineRunner initEvents() {
//        return args -> {
//            Events event = eventsRepository.save(
//                Events.builder()
//                    .name("Test Event")
//                    .type("music")
//                    .description("Test Event Description")
//                    .startDate(Instant.now())
//                    .endDate(Instant.now())
//                    .location("Test Location")
//                    .organizer(usersRepository.findById(userId).get())
//                    .metadata("{ \"key\": \"value\" }")
//                    .build());
//            System.out.println("Event created: " + event.getId());
//        };
//    }
}
