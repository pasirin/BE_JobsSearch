package com.example.JobsSearch.model.util;

import lombok.Data;

import javax.persistence.*;

@Data
@Entity
@Table(name = "salaries")
public class Salary {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private Double daily;
    private Double hourly;
    private Double monthly;
    private Integer type;

    private String dailyText = "";

    private String hourlyText = "";

    private String monthlyText = "";

    public Long getId() {
        return id;
    }

    public Double getDaily() {
        return daily;
    }

    public void setDaily(Double daily) {
        this.daily = daily;
    }

    public Double getHourly() {
        return hourly;
    }

    public void setHourly(Double hourly) {
        this.hourly = hourly;
    }

    public Double getMonthly() {
        return monthly;
    }

    public void setMonthly(Double monthly) {
        this.monthly = monthly;
    }

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }

    public String getDailyText() {
        return dailyText;
    }

    public void setDailyText(String dailyText) {
        this.dailyText = dailyText;
    }

    public String getHourlyText() {
        return hourlyText;
    }

    public void setHourlyText(String hourlyText) {
        this.hourlyText = hourlyText;
    }

    public String getMonthlyText() {
        return monthlyText;
    }

    public void setMonthlyText(String monthlyText) {
        this.monthlyText = monthlyText;
    }

    public Salary(Long id, Double daily, Double hourly, Double monthly, Integer type, String dailyText, String hourlyText, String monthlyText) {
        this.id = id;
        this.daily = daily;
        this.hourly = hourly;
        this.monthly = monthly;
        this.type = type;
        this.dailyText = dailyText;
        this.hourlyText = hourlyText;
        this.monthlyText = monthlyText;
    }

    public Salary() {
    }

}
