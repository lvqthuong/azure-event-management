package com.example.fs19_azure.utility;

import com.example.fs19_azure.entity.Events;
import com.example.fs19_azure.entity.Users;
import com.example.fs19_azure.repository.EventsRepository;
import lombok.extern.log4j.Log4j2;
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

    private UUID userId;

    @Bean
    CommandLineRunner initData() {
        return args -> {
            initUsers();
            initEvents();
        };
    }

    private void initUsers() {
        Users user = usersRepository.save(
            Users.builder()
//                    .id(UUID.fromString("00000000-0000-0000-0000-000000000000"))
                .email("test@fs19java.com")
                .name("Test User")
                .password("password")
                .build()
        );
        userId = usersRepository.findByEmail("test@fs19java.com").getId();
        System.out.println("User created: " + userId);
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

        System.out.println("Event created: " + event.getId());
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
