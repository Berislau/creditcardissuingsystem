package com.bmbank.creditcardissuingsystem.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.bmbank.creditcardissuingsystem.constants.StatusEnum;
import com.bmbank.creditcardissuingsystem.dto.PersonDTO;
import com.bmbank.creditcardissuingsystem.entity.Person;
import com.bmbank.creditcardissuingsystem.entity.Status;
import com.bmbank.creditcardissuingsystem.exception.InvalidStatusException;
import com.bmbank.creditcardissuingsystem.service.PersonService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

@WebMvcTest(PersonController.class)
class PersonControllerTest {

  @Autowired private MockMvc mockMvc;

  @MockBean private PersonService personService;
  private final String testOiB = "12345678901";

  @Test
  void whenCreatePerson_thenReturnStatusCreated() throws Exception {
    PersonDTO personDTO = new PersonDTO();
    personDTO.setStatusId(1L);
    personDTO.setOib(testOiB);
    personDTO.setFirstName("Test");
    personDTO.setLastName("Testic");
    Person person = convertToEntity(personDTO);

    when(personService.savePerson(any())).thenReturn(person);

    mockMvc
        .perform(
            post("/api/person")
                .contentType(MediaType.APPLICATION_JSON)
                .content(new ObjectMapper().writeValueAsString(personDTO)))
        .andExpect(status().isCreated())
        .andExpect(jsonPath("$.firstName").value(person.getFirstName()));
  }

  @Test
  void whenGetPersonByOib_thenReturnPerson() throws Exception {
    Person person = setUpPerson();
    when(personService.findByOib(anyString())).thenReturn(person);

    mockMvc
        .perform(get("/api/person/{oib}", "12345678901"))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.oib").value(person.getOib()));
  }

  @Test
  void whenDeletePerson_thenStatusNoContent() throws Exception {
    doNothing().when(personService).deletePerson(anyString());

    mockMvc.perform(delete("/api/person/{oib}", "12345678901")).andExpect(status().isNoContent());
  }

  @Test
  void whenGenerateFileForPerson_thenStatusCreated() throws Exception {
    doNothing().when(personService).generateFileForPerson(anyString());

    mockMvc.perform(post("/api/person/{oib}/file", "12345678901")).andExpect(status().isCreated());
  }

  private Person convertToEntity(PersonDTO personDTO) {
    Person person = new Person();
    person.setFirstName(personDTO.getFirstName());
    person.setLastName(personDTO.getLastName());
    person.setOib(personDTO.getOib());

    if (!StatusEnum.existsById(personDTO.getStatusId())) {
      throw new InvalidStatusException("Invalid status ID: " + person.getStatus().getId());
    }

    Long statusId = personDTO.getStatusId();
    person.setStatus(new Status(statusId, StatusEnum.valueOfId(statusId).name()));
    return person;
  }

  private Person setUpPerson() {
    Person person = new Person();
    Status status = new Status();
    status.setName(StatusEnum.INACTIVE.name());
    status.setId(StatusEnum.INACTIVE.getId());
    person.setStatus(status);
    person.setFirstName("Test");
    person.setLastName("Tester");
    person.setOib(testOiB);

    return person;
  }
}
