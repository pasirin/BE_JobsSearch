package com.example.JobsSearch.controller;

import com.example.JobsSearch.model.Job;
import com.example.JobsSearch.models.util.InteractionType;
import com.example.JobsSearch.payload.Response.ResponseObject;
import com.example.JobsSearch.service.impl.JobService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.persistence.EntityNotFoundException;
import javax.validation.Valid;
import java.time.LocalDateTime;
import java.util.List;

@RestController
public class JobController {

    @Autowired
    JobService jobService;

    @GetMapping("/organization/{organizationId}/jobs")
    @PreAuthorize("hasRole('ROLE_HR')")
    public ResponseEntity<?> getAllJobsByOrganization(@Valid @PathVariable Long organizationId) {
        ResponseObject output = jobService.getAllJobsByOrganization(organizationId);
        return output.getStatus() ? ResponseEntity.ok().body(output.getData())
                : ResponseEntity.badRequest().body(output.getMessage());
    }

    @GetMapping("/jobs/{jobsId}")
    public ResponseEntity<?> getJobById(@Valid @PathVariable Long jobsId) {
        return ResponseEntity.ok().body(jobService.getById(jobsId));
    }

    @GetMapping("/seeker/{seekerId}/jobs?s=")
    @PreAuthorize("hasRole('ROLE_SEEKER')")
    public ResponseEntity<?> searchJob(
            @PathVariable Long seekerId,
            @RequestParam(required = false) String city,
            @RequestParam(required = false) String startTime,
            @RequestParam(required = false) String endTime
    ) {
        LocalDateTime startDateTime = null;
        LocalDateTime endDateTime = null;

        // Chuyển đổi thời gian từ String sang LocalDateTime nếu có
        if (startTime != null && !startTime.isEmpty()) {
            startDateTime = LocalDateTime.parse(startTime);
        }

        if (endTime != null && !endTime.isEmpty()) {
            endDateTime = LocalDateTime.parse(endTime);
        }

        List<Job> jobs = jobService.searchJobs(seekerId, city, startDateTime, endDateTime);
        return ResponseEntity.ok(jobs);
    }

    @PostMapping("/seeker/{seekerId}/jobs/{jobId}/interact")
    @PreAuthorize("hasRole('ROLE_SEEKER')")
    public ResponseEntity<String> interactWithJob(
            @PathVariable Long seekerId,
            @PathVariable Long jobId,
            @RequestParam String interactionType
    ) {
        InteractionType type;
        try {
            type = InteractionType.valueOf(interactionType.toUpperCase());
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body("Invalid interaction type.");
        }

        try {
            jobService.interactWithJob(seekerId, jobId, type);
            return ResponseEntity.ok("Interaction recorded successfully.");
        } catch (EntityNotFoundException e) {
            return ResponseEntity.notFound().build();
        }
    }

}
