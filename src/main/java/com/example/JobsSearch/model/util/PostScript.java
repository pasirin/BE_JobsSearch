package com.example.JobsSearch.model.util;

import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Data
@Entity
public class PostScript {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String body;
    private Integer sortOrder;

    public PostScript() {
    }

    public PostScript(String body, Integer sortOrder) {
        this.body = body;
        this.sortOrder = sortOrder;
    }
}
