package com.example.fs19_azure;

import com.example.fs19_azure.dto.EventsCreate;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;
import com.fasterxml.jackson.databind.ObjectMapper;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class EventsIntegrationTest {
    @Autowired
    MockMvc mockMvc;

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
            mockMvc.perform(post("/events")
                .contentType("application/json")
                .content(new ObjectMapper().writeValueAsString(
                    new EventsCreate(
                        "Test Event"
                        , "Test Description"
                        , "Test Location"
                        , "2021-10-10T12:00:00Z"
                        , "2021-10-10T10:00:00Z"
                        , "00000000-0000-0000-0000-000000000000"
                    ))))
                .andExpect(status().isCreated())
                .andDo(print());
        }

        @Test
        void shouldNotBeAbleToCreateEventWithBlankName() throws Exception {
            mockMvc.perform(post("/events")
                .contentType("application/json")
                .content(new ObjectMapper().writeValueAsString(
                    new EventsCreate(
                        ""
                        , "Test Description"
                        , "Test Location"
                        , "2021-10-10T10:00:00Z"
                        , "2021-10-10T12:00:00Z"
                        , "00000000-0000-0000-0000-000000000000"
                    ))))
                .andExpect(status().isBadRequest())
                .andDo(print());
        }

        @Test
        void shouldNotBeAbleToCreateEventWithBlankLocation() throws Exception {
            mockMvc.perform(post("/events")
                    .contentType("application/json")
                    .content(new ObjectMapper().writeValueAsString(
                        new EventsCreate(
                            "Test Event"
                            , "Test Description"
                            , ""
                            , "2021-10-10T10:00:00Z"
                            , "2021-10-10T12:00:00Z"
                            , "00000000-0000-0000-0000-000000000000"
                        ))))
                .andExpect(status().isBadRequest())
                .andDo(print());
        }
    }
}
