package com.bmbank.creditcardissuingsystem.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.bmbank.creditcardissuingsystem.constants.StatusEnum;
import com.bmbank.creditcardissuingsystem.dto.UserDTO;
import com.bmbank.creditcardissuingsystem.entity.Status;
import com.bmbank.creditcardissuingsystem.entity.User;
import com.bmbank.creditcardissuingsystem.exception.InvalidStatusException;
import com.bmbank.creditcardissuingsystem.service.UserService;
import com.fasterxml.jackson.databind.ObjectMapper;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

@WebMvcTest(UserController.class)
class UserControllerTest {

    @Autowired private MockMvc mockMvc;

    @MockBean private UserService userService;
    private final String testOiB = "25915070587";

    @Test
    void whenCreateUser_thenReturnStatusCreated() throws Exception {
        UserDTO userDTO = new UserDTO();
        userDTO.setStatusId(1L);
        userDTO.setOib(testOiB);
        userDTO.setFirstName("Test");
        userDTO.setLastName("Testic");
        User user = convertToEntity(userDTO);

        when(userService.saveUser(any())).thenReturn(user);

        mockMvc.perform(
                        post("/api/user")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(new ObjectMapper().writeValueAsString(userDTO)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.firstName").value(user.getFirstName()));
    }

    @Test
    void whenGetUserByOib_thenReturnUser() throws Exception {
        User user = setUpUser();
        when(userService.findByOib(anyString())).thenReturn(user);

        mockMvc.perform(get("/api/user/{oib}", testOiB))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.oib").value(user.getOib()));
    }

    @Test
    void whenDeleteUser_thenStatusNoContent() throws Exception {
        doNothing().when(userService).deleteUser(anyString());

        mockMvc.perform(delete("/api/user/{oib}", testOiB)).andExpect(status().isNoContent());
    }

    @Test
    void whenGenerateFileForUser_thenStatusCreated() throws Exception {
        doNothing().when(userService).generateFileForUser(anyString());

        mockMvc.perform(post("/api/user/{oib}/file", testOiB)).andExpect(status().isCreated());
    }

    private User convertToEntity(UserDTO userDTO) {
        User user = new User();
        user.setFirstName(userDTO.getFirstName());
        user.setLastName(userDTO.getLastName());
        user.setOib(userDTO.getOib());

        if (!StatusEnum.existsById(userDTO.getStatusId())) {
            throw new InvalidStatusException("Invalid status ID: " + user.getStatus().getId());
        }

        Long statusId = userDTO.getStatusId();
        Status status = new Status();
        status.setId(statusId);
        status.setName(StatusEnum.valueOfId(statusId).name());
        user.setStatus(status);
        return user;
    }

    private User setUpUser() {
        User user = new User();
        Status status = new Status();
        status.setName(StatusEnum.INACTIVE.name());
        status.setId(StatusEnum.INACTIVE.getId());
        user.setStatus(status);
        user.setFirstName("Test");
        user.setLastName("Tester");
        user.setOib(testOiB);

        return user;
    }
}
