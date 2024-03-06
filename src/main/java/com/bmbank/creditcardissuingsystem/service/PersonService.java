package com.bmbank.creditcardissuingsystem.service;

import com.bmbank.creditcardissuingsystem.configuration.AppProperties;
import com.bmbank.creditcardissuingsystem.constants.Messages;
import com.bmbank.creditcardissuingsystem.constants.StatusEnum;
import com.bmbank.creditcardissuingsystem.entity.Person;
import com.bmbank.creditcardissuingsystem.entity.Status;
import com.bmbank.creditcardissuingsystem.exception.FileAlreadyExistsException;
import com.bmbank.creditcardissuingsystem.exception.FileStorageException;
import com.bmbank.creditcardissuingsystem.exception.InvalidStatusException;
import com.bmbank.creditcardissuingsystem.exception.PersonAlreadyExistsException;
import com.bmbank.creditcardissuingsystem.repository.PersonRepository;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Optional;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@AllArgsConstructor
public class PersonService {

  private final PersonRepository personRepository;

  private final AppProperties appProperties;

  @Transactional
  public Person savePerson(Person person) {
    log.info("Saving Person: {}", person);
    Optional<Person> existingPerson = personRepository.findByOib(person.getOib());
    if (existingPerson.isPresent()) {
      throw new PersonAlreadyExistsException(
          "Person with OIB: " + person.getOib() + " already exists.");
    }

    if (!StatusEnum.existsById(person.getStatus().getId())) {
      throw new InvalidStatusException("Invalid status ID: " + person.getStatus().getId());
    }
    return personRepository.save(person);
  }

  public Person findByOib(String oib) {
    log.info("Finding Person by OiB: {}", oib);
    return personRepository
        .findByOib(oib)
        .orElseThrow(() -> new EntityNotFoundException(Messages.PERSON_NOT_FOUND + oib));
  }

  @Transactional
  public void deletePerson(String oib) {
    log.info("Attempting to delete Person with OIB: {}", oib);
    Person person =
        personRepository
            .findByOib(oib)
            .orElseThrow(() -> new EntityNotFoundException(Messages.PERSON_NOT_FOUND + oib));

    if (person.getFileName() != null && !person.getFileName().isEmpty()) {
      Status status = new Status(StatusEnum.INACTIVE.getId(), StatusEnum.INACTIVE.name());
      person.setStatus(status);
      writeToFileOnStorage(person, person.getFileName());
      log.info("File {} for Person with OIB: {} marked as INACTIVE", person.getFileName(), oib);
    }

    personRepository.deleteByOib(oib);
    log.info("Person with OIB: {} has been deleted", oib);
  }

  @Transactional
  public void generateFileForPerson(String oib) {
    log.info("Generating file for Person with OIB: {}", oib);

    Person person =
        personRepository
            .findByOib(oib)
            .orElseThrow(() -> new EntityNotFoundException(Messages.PERSON_NOT_FOUND + oib));

    if (person.getFileName() != null && !person.getFileName().isEmpty()) {
      throw new FileAlreadyExistsException(
          "Active file already exists with file name: " + person.getFileName());
    }

    LocalDateTime now = LocalDateTime.now();
    person.setFileCreated(now);
    String timeStamp = now.format(DateTimeFormatter.ofPattern(Messages.TIME_PATTERN));
    String fileName = oib + "_" + timeStamp + ".txt";
    person.setFileName(fileName);
    person.setStatus(new Status(StatusEnum.ACTIVE.getId(), StatusEnum.ACTIVE.name()));

    personRepository.save(person);
    writeToFileOnStorage(person, fileName);
    log.info("File '{}' generated for Person with OIB: {}", person.getFileName(), oib);
  }

  private void writeToFileOnStorage(Person person, String fileName) {

    log.info("Attempting to write to file: {}", fileName);
    String fileContent =
        String.join(
            ",",
            person.getFirstName(),
            person.getLastName(),
            person.getOib(),
            person.getStatus().getName());

    try {
      Path path = Paths.get(appProperties.getFileStorageLocation()).resolve(fileName);
      log.debug("Creating directories for path: {}", path);
      Files.createDirectories(path.getParent());
      log.debug("Directory created");
      try (BufferedWriter writer = Files.newBufferedWriter(path)) {
        writer.write(fileContent);
      }
      log.info("Content written to file: {}", fileName);
    } catch (IOException e) {
      log.error("Failed to write file: {}", fileName, e);
      throw new FileStorageException("Could not store file " + fileName, e);
    }
  }
}
