package com.example.JobsSearch.payload.Request;

import com.example.JobsSearch.model.util.OrganizationType;
import lombok.Data;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;

@Data
public class HrSignupRequest {
    @NotBlank
    private String username;
    @NotBlank
    private String password;

    @NotBlank
    private String name;

    @NotBlank
    @Email
    private String email;

    @NotBlank
    private String phone_number;

    private String website;

    @NotBlank
    private String address;

    private String introduction;

    private OrganizationType organizationType;
}
