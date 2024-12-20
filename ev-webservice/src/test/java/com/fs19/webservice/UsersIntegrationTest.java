package com.fs19.webservice;

import com.fs19.webservice.dto.UsersCreate;
import com.fs19.webservice.exceptions.ErrorMessage;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;

import java.util.UUID;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;

@SpringBootTest
@AutoConfigureMockMvc
public class UsersIntegrationTest {
    @Autowired
    MockMvc mockMvc;

    String existingUUID = UUID.randomUUID().toString();

    @BeforeEach
    void setUp() throws Exception {
        String email = "test-user-" + existingUUID + "@fs19java.com";
        mockMvc.perform(post("/users")
            .contentType("application/json")
            .content(new ObjectMapper().writeValueAsString(
                new UsersCreate(
                    email
                    , "password"
                    , "User Test"
                ))));
    }

    @Nested
    class GetUsersTest {
        @Test
        void shouldBeAbleToGetAllUsers() throws Exception {
            mockMvc.perform(get("/users"))
                .andExpect(status().isOk())
                .andDo(print());
//                    .andExpect(jsonPath("$", hasSize(1)))
//                    .andExpect(jsonPath("$[0].name", is("John Doe")))
//                    .andExpect(jsonPath("$[0].email", is("
        }
    }

    @Nested
    class PostUsersTest {
        @Test
        void shouldBeAbleToCreateUser() throws Exception {
            String email = "new-user-" + UUID.randomUUID() + "@fs19java.com";
            mockMvc.perform(post("/users")
                .contentType("application/json")
                .content(new ObjectMapper().writeValueAsString(
                    new UsersCreate(
                        email
                        , "password"
                        , "New User"
                    ))))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.data.id").exists())
                .andExpect(jsonPath("$.data.name").value("New User"))
                .andExpect(jsonPath("$.data.email").value("new-user@fs19java.com"))
                .andDo(print());
        }

        @Test
        void shouldNotBeAbleToCreateUserWithExistingEmail() throws Exception {
            mockMvc.perform(post("/users")
                    .contentType("application/json")
                    .content(new ObjectMapper().writeValueAsString(
                        new UsersCreate(
                            "test-user-" + existingUUID + "@fs19java.com"
                            , "password"
                            , "User Test"
                        ))))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.errors[0].message").value(ErrorMessage.EMAIL_ALREADY_USED.getMessage()))
                .andDo(print());
        }
    }
}
