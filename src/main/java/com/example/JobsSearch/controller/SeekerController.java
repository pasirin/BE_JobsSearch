package com.example.JobsSearch.controller;

import com.example.JobsSearch.model.Seeker;
import com.example.JobsSearch.payload.Response.ResponseObject;
import com.example.JobsSearch.repository.SeekerRepository;
import com.example.JobsSearch.security.UserDetailsImpl;
import com.example.JobsSearch.service.impl.SeekerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/seeker")
@PreAuthorize("hasRole('SEEKER')")
public class SeekerController {
  @Autowired SeekerService seekerService;

  @GetMapping("/profile")
  public ResponseEntity<?> getProfile() {
    UserDetailsImpl userDetails =
        (UserDetailsImpl) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
    ResponseObject output = seekerService.getProfile(userDetails.getId());
    return output.getStatus()
        ? ResponseEntity.ok().body(output.getData())
        : ResponseEntity.badRequest().body(output.getMessage());
  }
}