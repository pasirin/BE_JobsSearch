package com.example.JobsSearch.model;

import lombok.Data;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import java.time.LocalDateTime;

@Data
@Entity
@Table(name = "news")
@EntityListeners(AuditingEntityListener.class)
public class News {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @CreatedDate
    private LocalDateTime createdAt;
    @LastModifiedDate
    private LocalDateTime updatedAt;
    private LocalDateTime opensAt;
    private LocalDateTime expiresAt;
    private String title;
    private String subTitle;
    private Integer category;   //1: Seminar , 2: corporate recruiting session, 3: general information
    private String body;
    private String eventPageUrl;
    private LocalDateTime eventStartAt;
    private LocalDateTime eventEndAt;

    public News() {
    }

    public News(LocalDateTime opensAt, LocalDateTime expiresAt, String title, String subTitle, Integer category, String body, String eventPageUrl, LocalDateTime eventStartAt, LocalDateTime eventEndAt) {

        this.opensAt = opensAt;
        this.expiresAt = expiresAt;
        this.title = title;
        this.subTitle = subTitle;
        this.category = category;
        this.body = body;
        this.eventPageUrl = eventPageUrl;
        this.eventStartAt = eventStartAt;
        this.eventEndAt = eventEndAt;
    }
}
