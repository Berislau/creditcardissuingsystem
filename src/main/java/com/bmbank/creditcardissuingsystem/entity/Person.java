package com.bmbank.creditcardissuingsystem.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import java.util.Objects;
import lombok.*;
import org.hibernate.Hibernate;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class Person {

  @Id private String oib;
  private String firstName;
  private String lastName;
  private String status;

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) return false;
    Person person = (Person) o;
    return oib != null && Objects.equals(oib, person.oib);
  }

  @Override
  public int hashCode() {
    return getClass().hashCode();
  }
}
