package com.bmbank.creditcardissuingsystem.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class PersonDTO {

  private String firstName;
  private String lastName;

  @Size(min = 11, max = 11, message = "OiB must be exactly 11 digits long")
  @Pattern(regexp = "\\d{11}", message = "OIB must be numeric")
  private String oib;

  @Schema(defaultValue = "1")
  private Long statusId;
}
