package com.bmbank.creditcardissuingsystem.service;

import com.bmbank.creditcardissuingsystem.configuration.AppProperties;
import com.bmbank.creditcardissuingsystem.constants.Messages;
import com.bmbank.creditcardissuingsystem.constants.StatusEnum;
import com.bmbank.creditcardissuingsystem.entity.Status;
import com.bmbank.creditcardissuingsystem.entity.User;
import com.bmbank.creditcardissuingsystem.exception.FileAlreadyExistsException;
import com.bmbank.creditcardissuingsystem.exception.FileStorageException;
import com.bmbank.creditcardissuingsystem.exception.InvalidStatusException;
import com.bmbank.creditcardissuingsystem.exception.UserAlreadyExistsException;
import com.bmbank.creditcardissuingsystem.repository.UserRepository;

import jakarta.persistence.EntityNotFoundException;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Optional;

@Service
@Slf4j
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;

    private final AppProperties appProperties;

    @Transactional
    public User saveUser(User user) {
        log.info("Saving User: {}", user);
        Optional<User> existingUser = userRepository.findByOib(user.getOib());
        if (existingUser.isPresent()) {
            throw new UserAlreadyExistsException(
                    "User with OIB: " + user.getOib() + " already exists.");
        }

        if (!StatusEnum.existsById(user.getStatus().getId())) {
            throw new InvalidStatusException("Invalid status ID: " + user.getStatus().getId());
        }
        return userRepository.save(user);
    }

    public User findByOib(String oib) {
        log.info("Finding User by OiB: {}", oib);
        return userRepository
                .findByOib(oib)
                .orElseThrow(() -> new EntityNotFoundException(Messages.USER_NOT_FOUND + oib));
    }

    @Transactional
    public void deleteUser(String oib) {
        log.info("Attempting to delete user with OIB: {}", oib);
        User user =
                userRepository
                        .findByOib(oib)
                        .orElseThrow(
                                () -> new EntityNotFoundException(Messages.USER_NOT_FOUND + oib));

        if (user.getFileName() != null && !user.getFileName().isEmpty()) {
            Status status = new Status();
            status.setId(StatusEnum.INACTIVE.getId());
            status.setName(StatusEnum.INACTIVE.name());
            user.setStatus(status);
            writeToFileOnStorage(user, user.getFileName());
            log.info("File {} for user with OIB: {} marked as INACTIVE", user.getFileName(), oib);
        }

        userRepository.deleteByOib(oib);
        log.info("User with OIB: {} has been deleted", oib);
    }

    @Transactional
    public void generateFileForUser(String oib) {
        log.info("Generating file for User with OIB: {}", oib);

        User user =
                userRepository
                        .findByOib(oib)
                        .orElseThrow(
                                () -> new EntityNotFoundException(Messages.USER_NOT_FOUND + oib));

        if (user.getFileName() != null && !user.getFileName().isEmpty()) {
            throw new FileAlreadyExistsException(
                    "Active file already exists with file name: " + user.getFileName());
        }

        LocalDateTime now = LocalDateTime.now();
        user.setFileCreated(now);
        String timeStamp = now.format(DateTimeFormatter.ofPattern(Messages.TIME_PATTERN));
        String fileName = oib + "_" + timeStamp + ".txt";
        user.setFileName(fileName);
        Status status = new Status();
        status.setId(StatusEnum.ACTIVE.getId());
        status.setName(StatusEnum.ACTIVE.name());
        user.setStatus(status);

        userRepository.save(user);
        writeToFileOnStorage(user, fileName);
        log.info("File '{}' generated for User with OIB: {}", user.getFileName(), oib);
    }

    private void writeToFileOnStorage(User user, String fileName) {

        log.info("Attempting to write to file: {}", fileName);
        String fileContent =
                String.join(
                        ",",
                        user.getFirstName(),
                        user.getLastName(),
                        user.getOib(),
                        user.getStatus().getName());

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
