package com.example.JobsSearch.model.util;

import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Data
@Entity
public class Property {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String body;
    private String title;
    private Integer sortOrder;
    private Boolean is_displayed;

    public Property() {
    }

    public Property(String body, String title, Integer sortOrder, Boolean is_displayed) {
        this.body = body;
        this.title = title;
        this.sortOrder = sortOrder;
        this.is_displayed = is_displayed;
    }
}
