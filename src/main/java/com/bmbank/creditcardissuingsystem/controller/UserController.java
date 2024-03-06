package com.bmbank.creditcardissuingsystem.controller;

import com.bmbank.creditcardissuingsystem.dto.UserDTO;
import com.bmbank.creditcardissuingsystem.entity.User;
import com.bmbank.creditcardissuingsystem.mapper.UserDTOConverter;
import com.bmbank.creditcardissuingsystem.service.UserService;
import com.bmbank.creditcardissuingsystem.validator.NameValidator;
import com.bmbank.creditcardissuingsystem.validator.OiBValidator;

import com.bmbank.creditcardissuingsystem.validator.StatusIdValidator;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;

import jakarta.validation.Valid;

import lombok.RequiredArgsConstructor;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/user")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;
    private final OiBValidator oiBValidator;
    private final NameValidator nameValidator;
    private final StatusIdValidator statusIdValidator;

    @Operation(
            summary = "Create a new user",
            description = "Creates a new user and stores it in the database.")
    @ApiResponse(responseCode = "201", description = "User created successfully")
    @PostMapping
    public ResponseEntity<User> createUser(@RequestBody @Valid UserDTO userDTO) {
        nameValidator.validateName(userDTO.getFirstName());
        nameValidator.validateName(userDTO.getLastName());
        oiBValidator.validateOiB(userDTO.getOib());
        statusIdValidator.validateStatusId(userDTO.getStatusId());
        User user = UserDTOConverter.convertToEntity(userDTO);
        User savedUser = userService.saveUser(user);
        return new ResponseEntity<>(savedUser, HttpStatus.CREATED);
    }

    @Operation(
            summary = "Get a user by OIB",
            description = "Retrieves a user's details by their OIB.")
    @ApiResponse(responseCode = "200", description = "User retrieved successfully")
    @GetMapping("/{oib}")
    public ResponseEntity<User> getUser(@PathVariable String oib) {
        oiBValidator.validateOiB(oib);
        User user = userService.findByOib(oib);
        return new ResponseEntity<>(user, HttpStatus.OK);
    }

    @Operation(
            summary = "Delete a user",
            description = "Deletes a user from the database by their OIB.")
    @ApiResponse(responseCode = "204", description = "User deleted successfully")
    @DeleteMapping("/{oib}")
    public ResponseEntity<Void> deleteUser(@PathVariable String oib) {
        oiBValidator.validateOiB(oib);
        userService.deleteUser(oib);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @Operation(
            summary = "Generate a file for a user",
            description = "Generates a new file for a user by their OIB.")
    @ApiResponse(responseCode = "201", description = "File created successfully")
    @PostMapping("/{oib}/file")
    public ResponseEntity<Void> generateFileForUser(@PathVariable String oib) {
        oiBValidator.validateOiB(oib);
        userService.generateFileForUser(oib);
        return new ResponseEntity<>(HttpStatus.CREATED);
    }
}
