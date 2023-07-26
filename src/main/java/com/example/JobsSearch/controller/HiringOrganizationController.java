package com.example.JobsSearch.controller;

import com.example.JobsSearch.model.HiringOrganization;
import com.example.JobsSearch.payload.Request.HrUpdateProfileRequest;
import com.example.JobsSearch.payload.Request.JobRequest;
import com.example.JobsSearch.payload.Response.ResponseObject;
import com.example.JobsSearch.security.UserDetailsImpl;
import com.example.JobsSearch.service.impl.HiringOrganizationService;
import com.example.JobsSearch.service.impl.JobService;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.Valid;
import java.time.LocalTime;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/organizations")
@PreAuthorize("hasRole('ROLE_HR')")
public class HiringOrganizationController {

  @Autowired HiringOrganizationService hiringOrganizationService;

  @Autowired JobService jobService;

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

  @PostMapping(value = "/jobs", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
  public ResponseEntity<?> createJob(@RequestBody JobRequest jobRequest) {
    UserDetailsImpl userDetails =
        (UserDetailsImpl) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
    ResponseObject output = jobService.create(userDetails.getId(), jobRequest);
    return output.getStatus()
        ? ResponseEntity.ok().build()
        : ResponseEntity.badRequest().body(output.getMessage());
  }
}
