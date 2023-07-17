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

    public Long getId() {
        return id;
    }


    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getDescription() {
        return description;
    }


    public void setDescription(String description) {
        this.description = description;
    }
}
