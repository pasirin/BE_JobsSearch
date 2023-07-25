package com.example.JobsSearch.controller;

import com.example.JobsSearch.model.Job;
import com.example.JobsSearch.model.Seeker;
import com.example.JobsSearch.models.util.InteractionType;
import com.example.JobsSearch.payload.Request.JobSearchRequest;
import com.example.JobsSearch.payload.Request.SeekerUpdateRequest;
import com.example.JobsSearch.payload.Response.ResponseObject;
import com.example.JobsSearch.repository.SeekerRepository;
import com.example.JobsSearch.security.UserDetailsImpl;
import com.example.JobsSearch.service.impl.JobService;
import com.example.JobsSearch.service.impl.SeekerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.persistence.EntityNotFoundException;
import javax.validation.Valid;
import java.time.LocalDateTime;
import java.util.Collection;

@RestController
@RequestMapping("/seeker")
@PreAuthorize("hasRole('SEEKER')")
public class SeekerController {
  @Autowired SeekerService seekerService;

  @Autowired JobService jobService;

  @GetMapping("/profile")
  public ResponseEntity<?> getProfile() {
    UserDetailsImpl userDetails =
        (UserDetailsImpl) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
    ResponseObject output = seekerService.getProfile(userDetails.getId());
    return output.getStatus()
        ? ResponseEntity.ok().body(output.getData())
        : ResponseEntity.badRequest().body(output.getMessage());
  }

  @PostMapping("/profile/update")
  public ResponseEntity<?> updateProfile(
      @Valid SeekerUpdateRequest seekerUpdateRequest,
      @RequestPart(required = false) MultipartFile image) {
    UserDetailsImpl userDetails =
        (UserDetailsImpl) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
    ResponseObject output = seekerService.update(userDetails.getId(), seekerUpdateRequest, image);
    return output.getStatus()
        ? ResponseEntity.ok().build()
        : ResponseEntity.badRequest().body(output.getMessage());
  }

  // Job Section
  @GetMapping("/jobs")
  public ResponseEntity<?> searchJob(@Valid JobSearchRequest jobSearchRequest) {
    UserDetailsImpl userDetails =
        (UserDetailsImpl) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
    Collection<Job> jobs = jobService.searchJobs(userDetails.getId(), jobSearchRequest);
    return ResponseEntity.ok(jobs);
  }

  @PostMapping("/jobs/{id}")
  public ResponseEntity<?> interactWithJob(
      @PathVariable Long id, @RequestParam InteractionType interactionType) {
    UserDetailsImpl userDetails =
        (UserDetailsImpl) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
    ResponseObject output = jobService.interactWithJob(userDetails.getId(), id, interactionType);
    return output.getStatus()
        ? ResponseEntity.ok().build()
        : ResponseEntity.badRequest().body(output.getMessage());
  }
}
