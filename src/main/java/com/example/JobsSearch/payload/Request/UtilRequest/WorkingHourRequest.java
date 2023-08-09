package com.example.JobsSearch.payload.Request.UtilRequest;

import lombok.Data;

import java.time.LocalTime;

@Data
public class WorkingHourRequest {
    private Integer hours;
    private LocalTime start_time;
    private LocalTime end_time;
    private Boolean is_full_time;
}
