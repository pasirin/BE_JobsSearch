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
    // Type Code Base Ex (0->none, 1 -> daily, 2->hourly, 3-> monthly, 4-> daily & hourly, ....)
    private Integer type;

    private String dailyText = "";

    private String hourlyText = "";

    private String monthlyText = "";

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
