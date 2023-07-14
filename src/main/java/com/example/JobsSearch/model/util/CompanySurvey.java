package com.example.JobsSearch.model.util;

import com.example.JobsSearch.model.Job;

import javax.persistence.*;
import java.util.List;

@Entity
public class CompanySurvey extends BaseContent {
    @Id
    private Long id;

    @OneToOne
    @MapsId
    @JoinColumn(name = "jobId")
    private Job job;

    public CompanySurvey(List<String> contents, boolean isDisplayed) {
        super(contents, isDisplayed);
    }

    public CompanySurvey() {
        super();
    }

    public Long getId() {
        return id;
    }


    public Job getJob() {
        return job;
    }

    public void setJob(Job job) {
        this.job = job;
    }
}
