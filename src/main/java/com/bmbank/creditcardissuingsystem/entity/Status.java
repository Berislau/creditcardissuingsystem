package com.bmbank.creditcardissuingsystem.entity;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;
import java.util.Objects;
import lombok.*;
import org.hibernate.Hibernate;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Entity
public class Status {

  @Id
  @Schema(accessMode = Schema.AccessMode.READ_ONLY)
  private Long id;

  @Column(nullable = false, unique = true)
  private String name;

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) return false;
    Status status = (Status) o;
    return id != null && Objects.equals(id, status.id);
  }

  @Override
  public int hashCode() {
    return getClass().hashCode();
  }
}
