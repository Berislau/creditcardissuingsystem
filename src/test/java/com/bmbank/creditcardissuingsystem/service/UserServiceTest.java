package com.bmbank.creditcardissuingsystem.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.bmbank.creditcardissuingsystem.configuration.AppProperties;
import com.bmbank.creditcardissuingsystem.constants.StatusEnum;
import com.bmbank.creditcardissuingsystem.entity.Status;
import com.bmbank.creditcardissuingsystem.entity.User;
import com.bmbank.creditcardissuingsystem.exception.UserAlreadyExistsException;
import com.bmbank.creditcardissuingsystem.repository.UserRepository;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Optional;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {

    @Mock private UserRepository userRepository;

    @Mock private AppProperties appProperties;

    @InjectMocks private UserService userService;

    private final String fileStorageLocation = "src/test/resources/file_out";
    private final String testOiB = "25915070587";

    @Test
    void whenSaveUser_thenUserIsSaved() {
        User user = setUpUser();
        when(userRepository.findByOib(anyString())).thenReturn(Optional.empty());
        when(userRepository.save(any(User.class))).thenReturn(user);

        userService.saveUser(user);

        verify(userRepository).save(any(User.class));
    }

    @Test
    void whenSaveUserWithExistingOib_thenThrowException() {
        User user = setUpUser();
        when(userRepository.findByOib(anyString())).thenReturn(Optional.of(user));

        assertThrows(UserAlreadyExistsException.class, () -> userService.saveUser(user));
    }

    @Test
    void whenFindByOib_thenUserIsReturned() {
        when(userRepository.findByOib(anyString())).thenReturn(Optional.of(new User()));

        userService.findByOib(testOiB);

        verify(userRepository).findByOib(anyString());
    }

    @Test
    void whenDeleteUser_thenUserIsDeleted() throws IOException {
        User user = setUpUser();
        user.setFileName("_20240503_000000.txt");
        when(userRepository.findByOib(anyString())).thenReturn(Optional.of(user));
        when(appProperties.getFileStorageLocation()).thenReturn(fileStorageLocation);

        userService.deleteUser(testOiB);

        verify(userRepository).deleteByOib(anyString());

        String expectedFileName = user.getFileName();
        Path expectedPath = Paths.get(fileStorageLocation, expectedFileName);
        String expectedContent =
                String.join(
                        ",",
                        user.getFirstName(),
                        user.getLastName(),
                        user.getOib(),
                        user.getStatus().getName());
        String fileContent = Files.readString(expectedPath);
        assertEquals(expectedContent, fileContent);

        Files.deleteIfExists(expectedPath);
    }

    @Test
    void whenGenerateFileForUser_thenFileIsGeneratedAndVerifyFileContent() throws Exception {
        User user = setUpUser();
        String oib = user.getOib();
        when(userRepository.findByOib(oib)).thenReturn(Optional.of(user));
        when(appProperties.getFileStorageLocation()).thenReturn(fileStorageLocation);

        userService.generateFileForUser(oib);

        verify(userRepository).save(user);
        String expectedFileName = user.getFileName();
        Path expectedPath = Paths.get(fileStorageLocation, expectedFileName);
        assertTrue(Files.exists(expectedPath));
        String expectedContent =
                String.join(
                        ",",
                        user.getFirstName(),
                        user.getLastName(),
                        user.getOib(),
                        user.getStatus().getName());
        String fileContent = Files.readString(expectedPath);
        assertEquals(expectedContent, fileContent);

        Files.deleteIfExists(expectedPath);
    }

    private User setUpUser() {
        User user = new User();
        Status status = new Status();
        status.setName(StatusEnum.INACTIVE.name());
        status.setId(StatusEnum.INACTIVE.getId());
        user.setStatus(status);
        user.setFirstName("Test");
        user.setLastName("Testic");
        user.setOib(testOiB);

        return user;
    }
}
