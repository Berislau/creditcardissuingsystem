package com.bmbank.creditcardissuingsystem.dto;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

@SpringBootTest
@AutoConfigureMockMvc
class UserDTOTest {

    @Autowired private MockMvc mockMvc;

    @Test
    void givenInvalidUserDTO_whenPostRequest_thenBadRequest() throws Exception {
        UserDTO invalidUserDTO = new UserDTO();
        invalidUserDTO.setFirstName("");
        invalidUserDTO.setLastName("");
        invalidUserDTO.setOib("123");
        invalidUserDTO.setStatusId(3L);

        mockMvc.perform(
                        post("/api/user")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(new ObjectMapper().writeValueAsString(invalidUserDTO)))
                .andExpect(status().isBadRequest());
    }
}
