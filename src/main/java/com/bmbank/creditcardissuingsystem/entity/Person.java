package com.bmbank.creditcardissuingsystem.entity;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import java.time.LocalDateTime;
import java.util.Objects;
import lombok.*;
import org.hibernate.Hibernate;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Entity
public class Person {

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
  @Pattern(regexp = "\\d{11}", message = "OIB must be numeric")
  private String oib;

  @ManyToOne
  @JoinColumn(nullable = false, name = "status_id", referencedColumnName = "id")
  private Status status;

  @Column private String fileName;

  @Column private LocalDateTime fileCreated;

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
