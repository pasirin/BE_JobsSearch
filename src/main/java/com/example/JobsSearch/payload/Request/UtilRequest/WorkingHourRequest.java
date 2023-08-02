package com.example.JobsSearch.payload.Request.UtilRequest;

import lombok.Data;

import java.time.LocalTime;

@Data
public class WorkingHourRequest {
    private Integer hours;
    private LocalTime startTime;
    private LocalTime endTime;
    private Boolean isFullTime;
}
