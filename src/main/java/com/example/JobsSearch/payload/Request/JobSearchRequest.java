package com.example.JobsSearch.payload.Request;

import lombok.Data;

import java.time.LocalTime;
import java.util.List;

@Data
public class JobSearchRequest {
    private String location1;
    private LocalTime startTime;
    private LocalTime endTime;
    private Boolean advanceSearch;
    private String location2;
    private String location3;
    private List<Long> tagId;
    private Boolean only_meta;
}
