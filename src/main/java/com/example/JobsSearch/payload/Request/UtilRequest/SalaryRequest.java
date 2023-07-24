package com.example.JobsSearch.payload.Request.UtilRequest;

import lombok.Data;

@Data
public class SalaryRequest {
    private Double daily;
    private Double hourly;
    private Double monthly;
    private String dailyText;
    private String hourlyText;
    private String monthlyText;
    private Integer type;
}
