package com.example.JobsSearch.model.util;

import lombok.Data;

import javax.persistence.*;
import java.util.List;

@MappedSuperclass
@Data
public abstract class BaseContent {

    @ElementCollection
    private List<String> contents;

    private boolean isDisplayed;

    public BaseContent() {

    }
    public BaseContent(List<String> contents, boolean isDisplayed) {
        this.contents = contents;
        this.isDisplayed = isDisplayed;
    }
}
