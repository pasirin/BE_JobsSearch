package com.example.JobsSearch.model.util;

import lombok.Data;

import javax.persistence.*;
import java.util.List;

@Data
@Entity
public class PhotoGallery {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinColumn(name = "photo_gallery_id")
    private List<Image> contents;
    private Boolean isDisplayed;

    public PhotoGallery(Long id) {
        this.id = id;
    }

    public PhotoGallery(List<Image> contents, Boolean isDisplayed) {
        this.contents = contents;
        this.isDisplayed = isDisplayed;
    }

    public PhotoGallery() {
    }

    public Long getId() {
        return id;
    }


    public List<Image> getContents() {
        return contents;
    }

    public void setContents(List<Image> contents) {
        this.contents = contents;
    }

    public Boolean getDisplayed() {
        return isDisplayed;
    }

    public void setDisplayed(Boolean displayed) {
        isDisplayed = displayed;
    }
}

