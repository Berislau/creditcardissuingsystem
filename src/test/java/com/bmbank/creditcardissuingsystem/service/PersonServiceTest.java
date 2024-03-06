package com.bmbank.creditcardissuingsystem.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.bmbank.creditcardissuingsystem.configuration.AppProperties;
import com.bmbank.creditcardissuingsystem.constants.StatusEnum;
import com.bmbank.creditcardissuingsystem.entity.Person;
import com.bmbank.creditcardissuingsystem.entity.Status;
import com.bmbank.creditcardissuingsystem.exception.PersonAlreadyExistsException;
import com.bmbank.creditcardissuingsystem.repository.PersonRepository;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Optional;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class PersonServiceTest {

  @Mock private PersonRepository personRepository;

  @Mock private AppProperties appProperties;

  @InjectMocks private PersonService personService;

  private final String fileStorageLocation = "src/test/resources/file_out";
  private final String testOiB = "12345678901";

  @Test
  void whenSavePerson_thenPersonIsSaved() {
    Person person = setUpPerson();
    when(personRepository.findByOib(anyString())).thenReturn(Optional.empty());
    when(personRepository.save(any(Person.class))).thenReturn(person);

    personService.savePerson(person);

    verify(personRepository).save(any(Person.class));
  }

  @Test
  void whenSavePersonWithExistingOib_thenThrowException() {
    Person person = setUpPerson();
    when(personRepository.findByOib(anyString())).thenReturn(Optional.of(person));

    assertThrows(PersonAlreadyExistsException.class, () -> personService.savePerson(person));
  }

  @Test
  void whenFindByOib_thenPersonIsReturned() {
    when(personRepository.findByOib(anyString())).thenReturn(Optional.of(new Person()));

    personService.findByOib(testOiB);

    verify(personRepository).findByOib(anyString());
  }

  @Test
  void whenDeletePerson_thenPersonIsDeleted() throws IOException {
    Person person = setUpPerson();
    person.setFileName("_20240503_000000.txt");
    when(personRepository.findByOib(anyString())).thenReturn(Optional.of(person));
    when(appProperties.getFileStorageLocation()).thenReturn(fileStorageLocation);

    personService.deletePerson(testOiB);

    verify(personRepository).deleteByOib(anyString());

    String expectedFileName = person.getFileName();
    Path expectedPath = Paths.get(fileStorageLocation, expectedFileName);
    String expectedContent =
        String.join(
            ",",
            person.getFirstName(),
            person.getLastName(),
            person.getOib(),
            person.getStatus().getName());
    String fileContent = Files.readString(expectedPath);
    assertEquals(expectedContent, fileContent);

    Files.deleteIfExists(expectedPath);
  }

  @Test
  void whenGenerateFileForPerson_thenFileIsGeneratedAndVerifyFileContent() throws Exception {
    Person person = setUpPerson();
    String oib = person.getOib();
    when(personRepository.findByOib(oib)).thenReturn(Optional.of(person));
    when(appProperties.getFileStorageLocation()).thenReturn(fileStorageLocation);

    personService.generateFileForPerson(oib);

    verify(personRepository).save(person);
    String expectedFileName = person.getFileName();
    Path expectedPath = Paths.get(fileStorageLocation, expectedFileName);
    assertTrue(Files.exists(expectedPath));
    String expectedContent =
        String.join(
            ",",
            person.getFirstName(),
            person.getLastName(),
            person.getOib(),
            person.getStatus().getName());
    String fileContent = Files.readString(expectedPath);
    assertEquals(expectedContent, fileContent);

    Files.deleteIfExists(expectedPath);
  }

  private Person setUpPerson() {
    Person person = new Person();
    Status status = new Status();
    status.setName(StatusEnum.INACTIVE.name());
    status.setId(StatusEnum.INACTIVE.getId());
    person.setStatus(status);
    person.setFirstName("Test");
    person.setLastName("Testic");
    person.setOib(testOiB);

    return person;
  }
}
