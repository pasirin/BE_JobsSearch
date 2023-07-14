package com.example.JobsSearch.model.util;

import com.example.JobsSearch.model.Job;
import com.example.JobsSearch.model.Seeker;

import javax.persistence.Embeddable;
import javax.persistence.ManyToOne;
import java.io.Serializable;

@Embeddable
public class SeekerJobId implements Serializable {
    @ManyToOne
    private Seeker seeker;

    @ManyToOne
    private Job job;

    public Seeker getSeeker() {
        return seeker;
    }

    public void setSeeker(Seeker seeker) {
        this.seeker = seeker;
    }

    public Job getJob() {
        return job;
    }

    public void setJob(Job job) {
        this.job = job;
    }
}
