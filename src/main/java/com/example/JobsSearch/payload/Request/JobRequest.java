package com.example.JobsSearch.payload.Request;

import com.example.JobsSearch.model.util.*;
import com.example.JobsSearch.payload.Request.UtilRequest.*;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

/**
 * temp
 */
@Data
public class JobRequest {
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate opensAt;
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate expiresAt;
    private String mainImageUrl;
    private String mainImageDescription;
    @NotBlank
    private String title;
    private String jobTitleCatchPhrase;
    @NotNull
    private Long locationId;
    private SalaryRequest salary;
    private List<WorkingHourRequest> workingHours;
    private List<Long> searchLabelIds;
    private String webApplicationUrl;
    private List<String> postScripts;
    private String catchText;
    private String leadText;
    private List<String> subImageUrl;
    private List<String> subImageDescriptions;
    private List<PropertyRequest> properties;
    private CompanySurveyRequest companySurvey;
    private BarometerRequest barometer;
    private List<String> galleryUrl;
    private List<String> galleryDescription;
    private InterviewRequest interview;
    private String productCode;
}
