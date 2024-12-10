package com.example.fs19_azure;

import com.example.fs19_azure.dto.EventsCreate;
import com.example.fs19_azure.dto.EventsUpdate;
import com.example.fs19_azure.dto.UsersCreate;
import com.example.fs19_azure.repository.EventsRepository;
import com.example.fs19_azure.repository.UsersRepository;
import com.fasterxml.jackson.core.JsonProcessingException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.util.UUID;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class EventsIntegrationTest {
    @Autowired
    MockMvc mockMvc;

    @Autowired
    private UsersRepository usersRepository;

    @Autowired
    private EventsRepository eventsRepository;

    private String eventMetadata = "{\n" +
        "  \"key\": \"value\"\n" +
        "}";
    private UUID userId;
    private UUID eventId;

    @BeforeEach
    void setup() throws Exception {
        mockMvc.perform(post("/users")
            .contentType("application/json")
            .content(new ObjectMapper().writeValueAsString(
                new UsersCreate(
                    "test-user@fs19java.com"
                    , "password"
                    , "User Test"
                ))));

        userId = usersRepository.findByEmail("test-user@fs19java.com").getId();

        mockMvc.perform(post("/events/create")
            .contentType("application/json")
            .content(new ObjectMapper().writeValueAsString(
                new EventsCreate(
                    "Test Event"
                    , "music"
                    ,"Test Description"
                    , "Test Location"
                    , "2021-10-10T12:00:00Z"
                    , "2021-10-10T10:00:00Z"
                    , userId.toString()
                    , eventMetadata
                    ,""
                ))));
        eventId = eventsRepository.findByName("Test Event").get(0).getId();
    }

    @Nested
    class GetEventsTest {
        @Test
        void shouldBeAbleToGetAllEvents() throws Exception {
            mockMvc.perform(get("/events"))
                .andExpect(status().isOk())
                .andDo(print());
//                    .andExpect(jsonPath("$", hasSize(1)))
//                    .andExpect(jsonPath("$[0].name", is("John Doe")))
//                    .andExpect(jsonPath("$[0].email", is("
        }
    }

    @Nested
    class PostEventsTest {
        @Test
        void shouldBeAbleToCreateEvent() throws Exception {

            mockMvc.perform(post("/events/create")
                .contentType("application/json")
                .content(new ObjectMapper().writeValueAsString(
                    new EventsCreate(
                        "Test Event"
                        , "music"
                        ,"Test Description"
                        , "Test Location"
                        , "2021-10-10T12:00:00Z"
                        , "2021-10-10T10:00:00Z"
                        , userId.toString()
                        , eventMetadata
                        ,""
                    ))))
                .andExpect(status().isCreated())
                .andDo(print());
        }

        @Test
        void shouldNotBeAbleToCreateEventWithBlankName() throws Exception {
            mockMvc.perform(post("/events/create")
                .contentType("application/json")
                .content(new ObjectMapper().writeValueAsString(
                    new EventsCreate(
                        ""
                        , "music"
                        ,"Test Description"
                        , "Test Location"
                        , "2021-10-10T12:00:00Z"
                        , "2021-10-10T10:00:00Z"
                        , userId.toString()
                        , eventMetadata
                        ,""
                    ))))
                .andExpect(status().isBadRequest())
                .andDo(print());
        }

        @Test
        void shouldNotBeAbleToCreateEventWithBlankLocation() throws Exception {
            mockMvc.perform(post("/events/create")
                    .contentType("application/json")
                    .content(new ObjectMapper().writeValueAsString(
                        new EventsCreate(
                            "Test Event"
                            , "music"
                            ,"Test Description"
                            , ""
                            , "2021-10-10T12:00:00Z"
                            , "2021-10-10T10:00:00Z"
                            , userId.toString()
                            , eventMetadata
                            ,""
                        ))))
                .andExpect(status().isBadRequest())
                .andDo(print());
        }
    }

    @Nested
    class UpdateEventsTest {

        @Test
        void shouldBeAbleToUpdateEventName() throws Exception {
            mockMvc.perform(put("/events/update/" + eventId)
                .contentType("application/json")
                .content(new ObjectMapper().writeValueAsString(
                    new EventsUpdate(
                        "Updated Event Name",
                        null,
                        null,
                        null,
                        null,
                        null,
                        null
                    )
                )))
                .andExpect(status().isOk())
                .andDo(print());
        }

        @Test
        void shouldNotBeAbleToUpdateWithInvalidFieldInJSON() throws Exception {
            mockMvc.perform(put("/events/update/" + eventId)
                    .contentType("application/json")
                    .content(new ObjectMapper().writeValueAsString(
                        "{\"organizerId\": \"00000000-0000-0000-0000-000000000000\"}"
                        )
                    ))
                .andExpect(status().isBadRequest())
                .andDo(print());
        }
    }

    @Nested
    class DeleteEventsTest {
        @Test
        void shouldReturnNotFoundIfDeleteFakeEvent() throws Exception {
            mockMvc.perform(delete("/events/delete/00000000-0000-0000-0000-000000000000"))
                .andExpect(status().isNotFound())
                .andDo(print());
        }
    }
}
