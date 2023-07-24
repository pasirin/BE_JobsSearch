package com.example.JobsSearch.model.util;

import com.example.JobsSearch.model.Job;
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
    private Job job;
    private String url;
    private Boolean is_available;

    public WebApplication() {
    }

    public WebApplication(String url) {
        this.url = url;
    }

    public WebApplication(String url, Boolean is_available) {
        this.url = url;
        this.is_available = is_available;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public Boolean getIs_available() {
        return is_available;
    }

    public void setIs_available(Boolean is_available) {
        this.is_available = is_available;
    }
}
