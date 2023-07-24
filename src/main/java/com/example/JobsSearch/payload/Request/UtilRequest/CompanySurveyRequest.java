package com.example.JobsSearch.payload.Request.UtilRequest;

import lombok.Data;

import java.util.List;

@Data
public class CompanySurveyRequest {
    private List<String> contents;
    private boolean isDisplayed;
}
