package com.bmbank.creditcardissuingsystem.repository;

import com.bmbank.creditcardissuingsystem.entity.User;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {

    Optional<User> findByOib(String oib);

    void deleteByOib(String oib);
}
