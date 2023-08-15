package com.example.JobsSearch.model.util;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import java.time.LocalTime;

@Data
@Entity
@AllArgsConstructor
@NoArgsConstructor
public class WorkingHour {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private Double countHours;

    private LocalTime startTime;

    private LocalTime endTime;

    private Boolean isFullTime = false;

    public WorkingHour(Double countHours, LocalTime startTime, LocalTime endTime, Boolean isFullTime) {
        this.countHours = countHours;
        this.startTime = startTime;
        this.endTime = endTime;
        this.isFullTime = isFullTime;
    }
}
