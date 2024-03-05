package com.bmbank.creditcardissuingsystem.constants;

public enum StatusEnum {
  INACTIVE(1L),
  ACTIVE(2L);

  private final Long id;

  StatusEnum(Long id) {
    this.id = id;
  }

  public Long getId() {
    return id;
  }

  public static StatusEnum valueOfId(Long id) {
    for (StatusEnum status : values()) {
      if (status.getId().equals(id)) {
        return status;
      }
    }
    throw new IllegalArgumentException("No status with id " + id + " found");
  }

  public static boolean existsById(Long id) {
    for (StatusEnum status : values()) {
      if (status.getId().equals(id)) {
        return true;
      }
    }
    return false;
  }
}
