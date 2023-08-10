package com.example.JobsSearch.model.util;

import lombok.Data;

import javax.persistence.*;

@Data
@Entity
public class Image {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String url;

    private String description;

    public Image(Long id, String url, String description) {
        this.id = id;
        this.url = url;
        this.description = description;
    }

    public Image() {
    }

    public Image(String url, String description) {
        this.url = url;
        this.description = description;
    }
}
