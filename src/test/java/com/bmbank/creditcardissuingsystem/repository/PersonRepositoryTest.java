package com.bmbank.creditcardissuingsystem.repository;

import static org.junit.jupiter.api.Assertions.*;

import com.bmbank.creditcardissuingsystem.constants.StatusEnum;
import com.bmbank.creditcardissuingsystem.entity.Person;
import com.bmbank.creditcardissuingsystem.entity.Status;
import java.util.Optional;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;

@DataJpaTest
class PersonRepositoryTest {

  @Autowired private TestEntityManager entityManager;

  @Autowired private PersonRepository personRepository;

  @Test
  void whenFindByOib_thenVerifyFoundPerson() {

    Status status = new Status();
    status.setName(StatusEnum.INACTIVE.name());
    status.setId(StatusEnum.INACTIVE.getId());
    entityManager.persist(status);

    Person person = new Person();
    person.setStatus(status);
    person.setFirstName("Test");
    person.setLastName("Tester");
    person.setOib("12345678901");
    entityManager.persist(person);
    entityManager.flush();

    Optional<Person> foundPerson = personRepository.findByOib(person.getOib());

    assertTrue(foundPerson.isPresent());
    assertEquals(person.getOib(), foundPerson.get().getOib());
    assertEquals(person.getFirstName(), foundPerson.get().getFirstName());
    assertEquals(person.getLastName(), foundPerson.get().getLastName());
    assertEquals(person.getStatus(), foundPerson.get().getStatus());
  }

  @Test
  void whenDeleteByOib_thenVerifyPersonIsDeleted() {

    Status status = new Status();
    status.setName(StatusEnum.INACTIVE.name());
    status.setId(StatusEnum.INACTIVE.getId());
    entityManager.persist(status);

    Person person = new Person();
    person.setStatus(status);
    person.setFirstName("Test");
    person.setLastName("Tester");
    person.setOib("12345678901");
    entityManager.persist(person);
    entityManager.flush();

    assertTrue(personRepository.findByOib(person.getOib()).isPresent());

    personRepository.deleteByOib(person.getOib());
    entityManager.flush();

    assertFalse(personRepository.findByOib(person.getOib()).isPresent());
  }
}
