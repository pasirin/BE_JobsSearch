package com.example.JobsSearch.payload.Request;

import lombok.Data;

import javax.validation.constraints.NotBlank;

@Data
public class SearchLabelRequest {
    @NotBlank
    private String name;

    private Boolean isEnable;
}
