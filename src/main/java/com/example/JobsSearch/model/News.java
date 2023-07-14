package com.example.JobsSearch.model;

import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import java.time.LocalDateTime;

@Entity
public class News {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @CreatedDate
    private LocalDateTime createdAt;

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    @LastModifiedDate
    private LocalDateTime updatedAt;

    private LocalDateTime opensAt;
    private LocalDateTime expiresAt;
    private String title;
    private String subTitle;
    private String category;
    private String body;
    private String eventPageUrl;
    private LocalDateTime eventStartAt;
    private LocalDateTime eventEndAt;

    public News() {
    }

    public News(Long id, LocalDateTime updatedAt, LocalDateTime opensAt, LocalDateTime expiresAt, String title, String subTitle, String category, String body, String eventPageUrl, LocalDateTime eventStartAt, LocalDateTime eventEndAt) {
        this.id = id;
        this.updatedAt = updatedAt;
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

    public Long getId() {
        return id;
    }

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(LocalDateTime updatedAt) {
        this.updatedAt = updatedAt;
    }

    public LocalDateTime getOpensAt() {
        return opensAt;
    }

    public void setOpensAt(LocalDateTime opensAt) {
        this.opensAt = opensAt;
    }

    public LocalDateTime getExpiresAt() {
        return expiresAt;
    }

    public void setExpiresAt(LocalDateTime expiresAt) {
        this.expiresAt = expiresAt;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getSubTitle() {
        return subTitle;
    }

    public void setSubTitle(String subTitle) {
        this.subTitle = subTitle;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }

    public String getEventPageUrl() {
        return eventPageUrl;
    }

    public void setEventPageUrl(String eventPageUrl) {
        this.eventPageUrl = eventPageUrl;
    }

    public LocalDateTime getEventStartAt() {
        return eventStartAt;
    }

    public void setEventStartAt(LocalDateTime eventStartAt) {
        this.eventStartAt = eventStartAt;
    }

    public LocalDateTime getEventEndAt() {
        return eventEndAt;
    }

    public void setEventEndAt(LocalDateTime eventEndAt) {
        this.eventEndAt = eventEndAt;
    }
}
