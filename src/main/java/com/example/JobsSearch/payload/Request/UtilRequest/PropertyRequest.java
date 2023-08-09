package com.example.JobsSearch.payload.Request.UtilRequest;

import lombok.Data;

@Data
public class PropertyRequest {
    private String body;
    private String title;
    private Integer sort_order;
    private Boolean is_displayed;
}
