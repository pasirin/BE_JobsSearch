package com.example.JobsSearch.model;

import com.example.JobsSearch.model.util.HistoryId;
import com.example.JobsSearch.model.util.InteractionType;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import java.time.LocalDateTime;

@Data
@Entity
@Table(name = "histories")
@AssociationOverrides({
        @AssociationOverride(name = "primaryKey.seeker", joinColumns = @JoinColumn(name = "seekerId")),
        @AssociationOverride(name = "primaryKey.job", joinColumns = @JoinColumn(name = "jobId"))
})
@NoArgsConstructor
@EntityListeners(AuditingEntityListener.class)
public class History {
    @EmbeddedId
    private HistoryId primaryKey;

    @CreatedDate
    private LocalDateTime createdAt;

    @LastModifiedDate
    private LocalDateTime updatedAt;

    @Enumerated(EnumType.STRING)
    private InteractionType interactionType;


    public History(HistoryId historyId, InteractionType interactionType) {
        this.primaryKey = historyId;
        this.interactionType = interactionType;
    }

    @Transient
    public Seeker getSeeker() {
        return getPrimaryKey().getSeeker();
    }

    @Transient
    public Job getJob() {
        return getPrimaryKey().getJob();
    }
}
