package com.example.JobsSearch.controller;

import com.example.JobsSearch.payload.Request.HrUpdateProfileRequest;
import com.example.JobsSearch.payload.Request.JobRequest;
import com.example.JobsSearch.payload.Response.ResponseObject;
import com.example.JobsSearch.security.UserDetailsImpl;
import com.example.JobsSearch.service.impl.HiringOrganizationService;
import com.example.JobsSearch.service.impl.JobService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/organizations")
@PreAuthorize("hasRole('ROLE_HR')")
public class HiringOrganizationController {

    @Autowired
    HiringOrganizationService hiringOrganizationService;

    @Autowired
    JobService jobService;

    @GetMapping("/profile")
    public ResponseEntity<?> getProfile() {
        UserDetailsImpl userDetails =
                (UserDetailsImpl) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        ResponseObject output = hiringOrganizationService.getProfile(userDetails.getId());
        return output.getStatus()
                ? ResponseEntity.ok().body(output.getData())
                : ResponseEntity.badRequest().body(output.getMessage());
    }

    @PutMapping("/profile/update")
    public ResponseEntity<?> updateHrProfile(
            @RequestBody HrUpdateProfileRequest hrUpdateProfileRequest) {
        ResponseObject output =
                hiringOrganizationService.updateProfile(
                        SecurityContextHolder.getContext().getAuthentication(), hrUpdateProfileRequest);
        return output.getStatus()
                ? ResponseEntity.ok().body(output.getData())
                : ResponseEntity.badRequest().body(output.getMessage());
    }

    @GetMapping("/jobs")
    public ResponseEntity<?> getAllJobsByOrganization() {
        UserDetailsImpl userDetails =
                (UserDetailsImpl) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        ResponseObject output = jobService.getAllJobsByOrganization(userDetails.getId());
        return output.getStatus()
                ? ResponseEntity.ok().body(output.getData())
                : ResponseEntity.badRequest().body(output.getMessage());
    }

    @GetMapping("/jobs/{jobId}")
    public ResponseEntity<?> getJobById(@PathVariable @Valid Long jobId) {
        UserDetailsImpl userDetails =
                (UserDetailsImpl) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        ResponseObject output = jobService.getJobByIdByOrganization(userDetails.getId(), jobId);
        return output.getStatus()
                ? ResponseEntity.ok().body(output.getData())
                : ResponseEntity.badRequest().body(output.getMessage());
    }

    @GetMapping("/jobs/{jobId}/seeker")
    public ResponseEntity<?> getAllSeekerLikeJob(@PathVariable @Valid Long jobId) {
        UserDetailsImpl userDetails =
                (UserDetailsImpl) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        ResponseObject output = jobService.getLikedSeekerHistoriesByJobId(userDetails.getId(), jobId);
        return output.getStatus()
                ? ResponseEntity.ok().body(output.getData())
                : ResponseEntity.badRequest().body(output.getMessage());
    }

    @PostMapping(value = "/jobs/create")
    public ResponseEntity<?> createJob(@RequestBody JobRequest jobRequest) {
        UserDetailsImpl userDetails =
                (UserDetailsImpl) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        ResponseObject output = jobService.create(userDetails.getId(), jobRequest);
        return output.getStatus()
                ? ResponseEntity.ok().build()
                : ResponseEntity.badRequest().body(output.getMessage());
    }

    @DeleteMapping("/jobs/{jobId}/delete")
    public ResponseEntity<?> deleteJob(@PathVariable Long jobId) {
        UserDetailsImpl userDetails =
                (UserDetailsImpl) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        ResponseObject output = jobService.delete(userDetails.getId(), jobId);
        return output.getStatus()
                ? ResponseEntity.ok().build()
                : ResponseEntity.badRequest().body(output.getMessage());
    }


    @PutMapping(value = "/jobs/{id}/update")
    public ResponseEntity<?> updateJob(@PathVariable Long id, @RequestBody JobRequest jobRequest) {
        UserDetailsImpl userDetails =
                (UserDetailsImpl) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        ResponseObject output = jobService.update(id, userDetails.getId(), jobRequest);
        return output.getStatus()
                ? ResponseEntity.ok().build()
                : ResponseEntity.badRequest().body(output.getMessage());
    }


}

