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
    private Integer hours;

    private LocalTime start;

    private LocalTime end;

    private Boolean is_full_time = false;

    public WorkingHour(Integer hours, LocalTime start, LocalTime end, Boolean is_full_time) {
        this.hours = hours;
        this.start = start;
        this.end = end;
        this.is_full_time = is_full_time;
    }
}
