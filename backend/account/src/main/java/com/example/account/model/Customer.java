package com.example.account.model;

import jakarta.persistence.Entity;

@Entity
public class Customer extends AuditedEntity {

  private String name;

  private String surname;

  public Customer() {}

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public String getSurname() {
    return surname;
  }

  public void setSurname(String surname) {
    this.surname = surname;
  }
}
