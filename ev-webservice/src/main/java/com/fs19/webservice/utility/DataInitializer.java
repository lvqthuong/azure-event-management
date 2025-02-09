package com.fs19.webservice.utility;

import com.fs19.webservice.entity.Events;
import com.fs19.webservice.entity.Users;
import com.fs19.webservice.repository.EventsRepository;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;

import com.fs19.webservice.repository.UsersRepository;

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
        UUID newUserId = UUID.randomUUID();
        Users user = usersRepository.save(
            Users.builder()
                .email(newUserId + "-test@fs19java.com")
                .name("Test User-" + newUserId)
                .password("password")
                .build()
        );
        userId = usersRepository.findByEmail(newUserId + "-test@fs19java.com").getId();
        log.info("User created: {}", userId);
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

        log.info("Event created: {}", event.getId());
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
