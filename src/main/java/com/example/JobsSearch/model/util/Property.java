package com.example.JobsSearch.model.util;

import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Data
@Entity
public class Property {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String body;
    private String title;
    private Integer sort_order;
    private Boolean is_displayed;

    public Property() {
    }

    public Property(String body, String title, Integer sort_order, Boolean is_displayed) {
        this.body = body;
        this.title = title;
        this.sort_order = sort_order;
        this.is_displayed = is_displayed;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Integer getSort_order() {
        return sort_order;
    }

    public void setSort_order(Integer sort_order) {
        this.sort_order = sort_order;
    }

    public Boolean getIs_displayed() {
        return is_displayed;
    }

    public void setIs_displayed(Boolean is_displayed) {
        this.is_displayed = is_displayed;
    }
}
