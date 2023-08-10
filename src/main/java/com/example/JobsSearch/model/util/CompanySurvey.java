package com.example.JobsSearch.model.util;

import com.example.JobsSearch.model.Job;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.persistence.*;
import java.util.List;

@EqualsAndHashCode(callSuper = true)
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
}
