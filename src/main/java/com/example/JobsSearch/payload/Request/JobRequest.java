package com.example.JobsSearch.payload.Request;

import com.example.JobsSearch.model.util.WorkingHour;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

/**
 * temp
 */
@Data
public class JobRequest {
    private LocalDateTime opensAt;
    private LocalDateTime expiresAt;
    private Long mainImageId;
    private String title;
    private String jobTitleCatchPhrase;
    private Long locationId;
    private Long salaryId;
    private List<WorkingHour> workingHours;
    private List<Long> searchLabelIds;
    private Long webApplicationId;
    private List<Long> postScriptIds;
    private String catchText;
    private String leadText;
    private List<Long> subImageIds;
    private List<Long> propertyIds;
    private Long companySurveyId;
    private Long barometerId;
    private Long photoGalleryId;
    private Long interviewId;
    private String productCode;
}
