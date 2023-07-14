package com.example.JobsSearch.model.util;

import javax.persistence.*;
import java.util.List;

@MappedSuperclass
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

    public List<String> getContents() {
        return contents;
    }

    public void setContents(List<String> contents) {
        this.contents = contents;
    }

    public boolean isDisplayed() {
        return isDisplayed;
    }

    public void setDisplayed(boolean displayed) {
        isDisplayed = displayed;
    }
}
