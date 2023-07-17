package com.example.JobsSearch.model;

import com.example.JobsSearch.model.util.SeekerJobId;
import lombok.Data;
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
@EntityListeners(AuditingEntityListener.class)
public class History {
    @EmbeddedId
    private SeekerJobId primaryKey;

    @CreatedDate
    private LocalDateTime createdAt;

    @LastModifiedDate
    private LocalDateTime updatedAt;


    private Integer action;

    public History(SeekerJobId primaryKey, LocalDateTime createdAt, LocalDateTime updatedAt, Integer action) {
        this.primaryKey = primaryKey;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
        this.action = action;
    }

    public History() {
    }

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(LocalDateTime updatedAt) {
        this.updatedAt = updatedAt;
    }

    public SeekerJobId getPrimaryKey() {
        return primaryKey;
    }

    @Transient
    public Seeker getSeeker() {
        return getPrimaryKey().getSeeker();
    }

    @Transient
    public Job getJob() {
        return getPrimaryKey().getJob();
    }

    public void setPrimaryKey(SeekerJobId primaryKey) {
        this.primaryKey = primaryKey;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public Integer getAction() {
        return action;
    }

    public void setAction(Integer action) {
        this.action = action;
    }

}
