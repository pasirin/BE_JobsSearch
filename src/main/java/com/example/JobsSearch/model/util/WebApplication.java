package com.example.JobsSearch.model.util;

import com.example.JobsSearch.model.Job;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;

import javax.persistence.*;

@Data
@Entity
public class WebApplication {
    @Id
    private Long id;

    @OneToOne
    @MapsId
    @JoinColumn(name = "jobId")
    @JsonIgnore
    private Job job;
    private String url;
    private Boolean isAvailable;

    public WebApplication() {
    }

    public WebApplication(String url) {
        this.url = url;
    }

    public WebApplication(String url, Boolean isAvailable) {
        this.url = url;
        this.isAvailable = isAvailable;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public Boolean getIsAvailable() {
        return isAvailable;
    }

    public void setIsAvailable(Boolean isAvailable) {
        this.isAvailable = isAvailable;
    }
}
