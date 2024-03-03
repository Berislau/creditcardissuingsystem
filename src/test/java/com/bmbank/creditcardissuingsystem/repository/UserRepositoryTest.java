package com.bmbank.creditcardissuingsystem.repository;

import static org.junit.jupiter.api.Assertions.*;

import com.bmbank.creditcardissuingsystem.constants.StatusEnum;
import com.bmbank.creditcardissuingsystem.entity.Status;
import com.bmbank.creditcardissuingsystem.entity.User;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;

import java.util.Optional;

@DataJpaTest
class UserRepositoryTest {

    @Autowired private TestEntityManager entityManager;

    @Autowired private UserRepository userRepository;

    @Test
    void whenFindByOib_thenVerifyFoundUser() {

        Status status = new Status();
        status.setName(StatusEnum.INACTIVE.name());
        status.setId(StatusEnum.INACTIVE.getId());
        entityManager.persist(status);

        User user = new User();
        user.setStatus(status);
        user.setFirstName("Test");
        user.setLastName("Tester");
        user.setOib("25915070587");
        entityManager.persist(user);
        entityManager.flush();

        Optional<User> foundUser = userRepository.findByOib(user.getOib());

        assertTrue(foundUser.isPresent());
        assertEquals(user.getOib(), foundUser.get().getOib());
        assertEquals(user.getFirstName(), foundUser.get().getFirstName());
        assertEquals(user.getLastName(), foundUser.get().getLastName());
        assertEquals(user.getStatus(), foundUser.get().getStatus());
    }

    @Test
    void whenDeleteByOib_thenVerifyUserIsDeleted() {

        Status status = new Status();
        status.setName(StatusEnum.INACTIVE.name());
        status.setId(StatusEnum.INACTIVE.getId());
        entityManager.persist(status);

        User user = new User();
        user.setStatus(status);
        user.setFirstName("Test");
        user.setLastName("Tester");
        user.setOib("25915070587");
        entityManager.persist(user);
        entityManager.flush();

        assertTrue(userRepository.findByOib(user.getOib()).isPresent());

        userRepository.deleteByOib(user.getOib());
        entityManager.flush();

        assertFalse(userRepository.findByOib(user.getOib()).isPresent());
    }
}
