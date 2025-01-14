package com.example.JobsSearch.payload.Request;


import lombok.Data;

import javax.validation.constraints.NotBlank;

@Data
public class SeekerSignupRequest {

    @NotBlank
    private String username;
    @NotBlank
    private String password;
}
