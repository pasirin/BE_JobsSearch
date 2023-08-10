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

    @OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.PERSIST)
    @JoinColumn(name = "photo_gallery_id")
    private List<Image> contents;

    public PhotoGallery(Long id) {
        this.id = id;
    }

    public PhotoGallery(List<Image> contents) {
        this.contents = contents;
    }

    public PhotoGallery() {
    }
}

