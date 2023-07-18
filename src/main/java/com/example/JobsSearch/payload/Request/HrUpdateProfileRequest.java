package com.example.JobsSearch.payload.Request;

import lombok.Data;

@Data
public class HrUpdateProfileRequest {
    private String name;

    private String email;

    private String phone_number;

    private String website;

    private String address;

    private String introduction;
}
