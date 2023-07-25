package com.example.JobsSearch.model.util;

import com.example.JobsSearch.model.Job;
import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import lombok.Data;

import javax.persistence.*;
import java.util.List;

@Data
@Entity
public class CompanySurvey extends BaseContent {
    @Id
    private Long id;

    @OneToOne
    @MapsId
    @JoinColumn(name = "id")
    @JsonIgnore
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
