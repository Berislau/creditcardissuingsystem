package com.bmbank.creditcardissuingsystem.controller;

import com.bmbank.creditcardissuingsystem.constants.StatusEnum;
import com.bmbank.creditcardissuingsystem.dto.PersonDTO;
import com.bmbank.creditcardissuingsystem.entity.Person;
import com.bmbank.creditcardissuingsystem.entity.Status;
import com.bmbank.creditcardissuingsystem.service.PersonService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/person")
@AllArgsConstructor
public class PersonController {

  private final PersonService personService;

  @Operation(
      summary = "Create a new person",
      description = "Creates a new person and stores it in the database.")
  @ApiResponse(responseCode = "201", description = "Person created successfully")
  @PostMapping
  public ResponseEntity<Person> createPerson(@RequestBody PersonDTO personDTO) {
    Person person = convertToEntity(personDTO);
    Person savedPerson = personService.savePerson(person);
    return new ResponseEntity<>(savedPerson, HttpStatus.CREATED);
  }

  @Operation(
      summary = "Get a person by OIB",
      description = "Retrieves a person's details by their OIB.")
  @ApiResponse(responseCode = "200", description = "Person retrieved successfully")
  @GetMapping("/{oib}")
  public ResponseEntity<Person> getPerson(@PathVariable String oib) {
    Person person = personService.findByOib(oib);
    return new ResponseEntity<>(person, HttpStatus.OK);
  }

  @Operation(
      summary = "Delete a person",
      description = "Deletes a person from the database by their OIB.")
  @ApiResponse(responseCode = "204", description = "Person deleted successfully")
  @DeleteMapping("/{oib}")
  public ResponseEntity<Void> deletePerson(@PathVariable String oib) {
    personService.deletePerson(oib);
    return new ResponseEntity<>(HttpStatus.NO_CONTENT);
  }

  @Operation(
      summary = "Generate a file for a person",
      description = "Generates a new file for a person by their OIB.")
  @ApiResponse(responseCode = "201", description = "File created successfully")
  @PostMapping("/{oib}/file")
  public ResponseEntity<Void> generateFileForPerson(@PathVariable String oib) {
    personService.generateFileForPerson(oib);
    return new ResponseEntity<>(HttpStatus.CREATED);
  }

  private Person convertToEntity(PersonDTO personDTO) {
    Person person = new Person();
    person.setFirstName(personDTO.getFirstName());
    person.setLastName(personDTO.getLastName());
    person.setOib(personDTO.getOib());

    if (!StatusEnum.existsById(personDTO.getStatusId())) {
      throw new RuntimeException("Invalid status ID: " + person.getStatus().getId());
    }

    Long statusId = personDTO.getStatusId();
    person.setStatus(new Status(statusId, StatusEnum.valueOfId(statusId).name()));
    return person;
  }
}
