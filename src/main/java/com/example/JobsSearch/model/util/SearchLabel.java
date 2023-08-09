package com.example.JobsSearch.model.util;

import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Data
@Entity
public class SearchLabel {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  private String name;
  private Boolean is_enabled;

  public SearchLabel() {}

  public Long getId() {
    return id;
  }

  public SearchLabel(String name, Boolean is_enabled) {
    this.name = name;
    this.is_enabled = is_enabled;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public Boolean getIs_enabled() {
    return is_enabled;
  }

  public void setIs_enabled(Boolean is_enabled) {
    this.is_enabled = is_enabled;
  }
}
