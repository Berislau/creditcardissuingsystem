package com.bmbank.creditcardissuingsystem.dto;

import io.swagger.v3.oas.annotations.media.Schema;

import lombok.Data;

@Data
public class UserDTO {

    @Schema(defaultValue = "John", description = "User's first name")
    private String firstName;

    @Schema(defaultValue = "Doe", description = "User's last name")
    private String lastName;

    @Schema(defaultValue = "25099753279", description = "User's OiB")
    private String oib;

    @Schema(defaultValue = "1", description = "1 for INACTIVE, 2 for ACTIVE status")
    private Long statusId;
}
