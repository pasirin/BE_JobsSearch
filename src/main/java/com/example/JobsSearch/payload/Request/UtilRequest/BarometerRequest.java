package com.example.JobsSearch.payload.Request.UtilRequest;

import lombok.Data;

import java.util.List;

@Data
public class BarometerRequest {
    private List<String> contents;
    private List<Integer> stats;
    private boolean isDisplayed;

}
