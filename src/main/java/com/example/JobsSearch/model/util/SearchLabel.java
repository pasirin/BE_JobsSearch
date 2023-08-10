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
  private Boolean isEnabled;

  public SearchLabel() {}

  public SearchLabel(String name, Boolean isEnabled) {
    this.name = name;
    this.isEnabled = isEnabled;
  }
}
