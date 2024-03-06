package com.bmbank.creditcardissuingsystem.entity;

import io.swagger.v3.oas.annotations.media.Schema;

import jakarta.persistence.*;
import jakarta.validation.constraints.Size;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.time.LocalDateTime;

@Getter
@Setter
@RequiredArgsConstructor
@ToString
@Entity
@Table(name = "users")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Schema(accessMode = Schema.AccessMode.READ_ONLY)
    private Long id;

    @Column(nullable = false)
    private String firstName;

    @Column(nullable = false)
    private String lastName;

    @Column(nullable = false, length = 11)
    @Size(min = 11, max = 11, message = "OiB must be exactly 11 digits long")
    private String oib;

    @ManyToOne
    @JoinColumn(nullable = false, name = "status_id", referencedColumnName = "id")
    private Status status;

    @Column private String fileName;

    @Column private LocalDateTime fileCreated;
}
