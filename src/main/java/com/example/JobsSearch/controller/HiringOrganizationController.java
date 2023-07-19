package com.example.JobsSearch.controller;

import com.example.JobsSearch.model.HiringOrganization;
import com.example.JobsSearch.payload.Request.HrUpdateProfileRequest;
import com.example.JobsSearch.payload.Response.ResponseObject;
import com.example.JobsSearch.security.UserDetailsImpl;
import com.example.JobsSearch.service.impl.HiringOrganizationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.Optional;

@RestController
@RequestMapping("/organizations")
@PreAuthorize("hasRole('ROLE_HR')")
public class HiringOrganizationController {

    @Autowired
    HiringOrganizationService hiringOrganizationService;

    @GetMapping("/profile")
    public ResponseEntity<?> getProfile() {
        UserDetailsImpl userDetails =
                (UserDetailsImpl) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        ResponseObject output = hiringOrganizationService.getProfile(userDetails.getId());
        return output.getStatus()
                ? ResponseEntity.ok().body(output.getData())
                : ResponseEntity.badRequest().body(output.getMessage());
    }

    @PutMapping("/update-profile")
    public ResponseEntity<?> updateHrProfile(@RequestBody HrUpdateProfileRequest hrUpdateProfileRequest) {
        ResponseObject output = hiringOrganizationService.updateProfile(SecurityContextHolder.getContext().getAuthentication(), hrUpdateProfileRequest);
        return output.getStatus()
                ? ResponseEntity.ok().body(output.getData())
                : ResponseEntity.badRequest().body(output.getMessage());
    }
}
