package com.example.JobsSearch.payload.Request.UtilRequest;

import lombok.Data;

@Data
public class ChangePasswordRequest {
    private String oldPassword;
    private String newPassword;
}
