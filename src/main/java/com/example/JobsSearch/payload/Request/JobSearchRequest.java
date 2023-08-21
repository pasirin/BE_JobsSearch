package com.example.JobsSearch.payload.Request;

import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalTime;
import java.util.List;

@Data
public class JobSearchRequest {
    private String location1;
    @DateTimeFormat(iso = DateTimeFormat.ISO.TIME)
    private LocalTime startTime;
    @DateTimeFormat(iso = DateTimeFormat.ISO.TIME)
    private LocalTime endTime;
    private Boolean advanceSearch;
    private String location2;
    private String location3;
    private List<Long> tagId;
    private Boolean only_meta;
    private Integer startAtPagination;
    private Integer endAtPagination;
}
