package com.bmbank.creditcardissuingsystem.repository;

import com.bmbank.creditcardissuingsystem.entity.Person;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PersonRepository extends JpaRepository<Person, Long> {

  Optional<Person> findByOib(String oib);

  void deleteByOib(String oib);
}
