package com.example.JobsSearch.model.util;

import com.example.JobsSearch.model.Job;
import com.example.JobsSearch.model.Seeker;
import lombok.NoArgsConstructor;

import javax.persistence.Embeddable;
import javax.persistence.ManyToOne;
import java.io.Serializable;

@NoArgsConstructor
@Embeddable
public class HistoryId implements Serializable {
    @ManyToOne
    private Seeker seeker;

    @ManyToOne
    private Job job;

    public HistoryId(Seeker seeker, Job job) {
        this.seeker = seeker;
        this.job = job;
    }

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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        HistoryId historyId = (HistoryId) o;

        if (!seeker.equals(historyId.seeker)) return false;
        return job.equals(historyId.job);
    }

    @Override
    public int hashCode() {
        int result = seeker.hashCode();
        result = 31 * result + job.hashCode();
        return result;
    }
}
