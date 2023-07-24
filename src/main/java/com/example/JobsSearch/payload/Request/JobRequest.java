package com.example.JobsSearch.payload.Request;

import com.example.JobsSearch.model.util.*;
import com.example.JobsSearch.payload.Request.UtilRequest.*;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDateTime;
import java.util.List;

/**
 * temp
 */
@Data
public class JobRequest {
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime opensAt;
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime expiresAt;
    private String mainImageDescription;
    private String title;
    private String jobTitleCatchPhrase;
    private Long locationId;
    private SalaryRequest salary;
    private List<WorkingHourRequest> workingHours;
    private List<Long> searchLabelIds;
    private String webApplicationUrl;
    private List<String> postScripts;
    private String catchText;
    private String leadText;
    private List<String> subImageDescriptions;
    private List<PropertyRequest> properties;
    private CompanySurveyRequest companySurvey;
    private BarometerRequest barometer;
    private List<String> galleryDescription;
    private InterviewRequest interview;
    private String productCode;
}
