package com.example.fs19_azure;

import com.example.fs19_azure.entity.Users;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;

@SpringBootTest
@AutoConfigureMockMvc
public class UsersIntegrationTest {
    @Autowired
    MockMvc mockMvc;

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
}
