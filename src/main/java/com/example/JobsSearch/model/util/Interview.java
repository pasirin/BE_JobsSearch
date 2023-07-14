package com.example.JobsSearch.model.util;

import com.example.JobsSearch.model.Job;

import javax.persistence.*;
import java.util.List;

@Entity
public class Interview extends BaseContent {
    @Id
    private Long id;

    @OneToOne
    @MapsId
    @JoinColumn(name = "jobId")
    private Job job;

    public Interview() {
    }

    public Interview(List<String> contents, boolean isDisplayed) {
        super(contents, isDisplayed);
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Job getJob() {
        return job;
    }

    public void setJob(Job job) {
        this.job = job;
    }
}