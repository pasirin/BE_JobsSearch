package com.example.JobsSearch.payload.Request;

import com.fasterxml.jackson.annotation.JsonFormat;
import org.springframework.format.annotation.DateTimeFormat;

import javax.validation.constraints.NotBlank;
import java.time.LocalDateTime;

public class NewsRequest {
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime opensAt;

    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime expiresAt;

    @NotBlank
    private String title;

    @NotBlank
    private String subTitle;

    @NotBlank
    private String category;

    @NotBlank
    private String body;

    @NotBlank
    private String eventPageUrl;

    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime eventStartAt;

    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime eventEndAt;

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
