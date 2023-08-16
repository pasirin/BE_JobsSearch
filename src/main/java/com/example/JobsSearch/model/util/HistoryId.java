package com.example.JobsSearch.model.util;

import com.example.JobsSearch.model.Job;
import com.example.JobsSearch.model.Seeker;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Embeddable;
import javax.persistence.ManyToOne;
import java.io.Serializable;

@NoArgsConstructor
@Embeddable
@Data
public class HistoryId implements Serializable {
    @ManyToOne
    private Seeker seeker;

    @ManyToOne
    private Job job;

    public HistoryId(Seeker seeker, Job job) {
        this.seeker = seeker;
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
}
