package com.example.JobsSearch.payload.Request.UtilRequest;

import lombok.Data;

@Data
public class PropertyRequest {
    private String body;
    private String title;
    private Integer sortOrder;
    private Boolean isDisplayed;
}
