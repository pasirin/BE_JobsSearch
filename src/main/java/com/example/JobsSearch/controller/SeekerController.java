package com.example.JobsSearch.controller;

import com.example.JobsSearch.model.util.InteractionType;
import com.example.JobsSearch.payload.Request.JobSearchRequest;
import com.example.JobsSearch.payload.Request.SeekerUpdateRequest;
import com.example.JobsSearch.payload.Response.ResponseObject;
import com.example.JobsSearch.security.UserDetailsImpl;
import com.example.JobsSearch.service.impl.JobService;
import com.example.JobsSearch.service.impl.SeekerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/seeker")
@PreAuthorize("hasRole('SEEKER')")
public class SeekerController {
  @Autowired SeekerService seekerService;

  @Autowired JobService jobService;

  @GetMapping("/profile")
  public ResponseEntity<?> getProfile() {
    UserDetailsImpl object;
    try {
      object =
          (UserDetailsImpl) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
    } catch (Exception e) {
      return ResponseEntity.status(HttpStatus.FORBIDDEN).body(e);
    }
    ResponseObject output = seekerService.getProfile(object.getId());
    return output.getStatus()
        ? ResponseEntity.ok().body(output.getData())
        : ResponseEntity.badRequest().body(output.getMessage());
  }

  @PostMapping("/profile/update")
  public ResponseEntity<?> updateProfile(
      @Valid @RequestBody SeekerUpdateRequest seekerUpdateRequest) {
    UserDetailsImpl userDetails =
        (UserDetailsImpl) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
    ResponseObject output = seekerService.update(userDetails.getId(), seekerUpdateRequest);
    return output.getStatus()
        ? ResponseEntity.ok().build()
        : ResponseEntity.badRequest().body(output.getMessage());
  }

  // Job Section
  @GetMapping("/jobs")
  public ResponseEntity<?> searchJob(@Valid JobSearchRequest jobSearchRequest) {
    UserDetailsImpl userDetails =
        (UserDetailsImpl) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
    ResponseObject output = jobService.searchJobs(userDetails.getId(), jobSearchRequest);
    return output.getStatus()
        ? ResponseEntity.ok().body(output.getData())
        : ResponseEntity.badRequest().body(output.getMessage());
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

  @GetMapping("/history")
  public ResponseEntity<?> getHistory() {
    UserDetailsImpl userDetails =
        (UserDetailsImpl) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
    ResponseObject output = seekerService.getSeekerHistory(userDetails.getId());
    return output.getStatus()
        ? ResponseEntity.ok().body(output.getData())
        : ResponseEntity.badRequest().body(output.getMessage());
  }
}
