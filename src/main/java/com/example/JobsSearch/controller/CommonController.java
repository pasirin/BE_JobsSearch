package com.example.JobsSearch.controller;

import com.example.JobsSearch.service.impl.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.Valid;
import java.util.List;

@RestController
public class CommonController {
  @Autowired NewsService newsService;

  @Autowired SearchLabelService searchLabelService;

  @Autowired LocationService locationService;

  @Autowired
  JobService jobService;

  @GetMapping("/news")
  public ResponseEntity<?> getAllNews() {
    return ResponseEntity.ok().body(newsService.getAll());
  }

  @GetMapping("/tag-search")
  public ResponseEntity<?> getAllSearchTag() {
    return ResponseEntity.ok().body(searchLabelService.getAll());
  }

  @GetMapping("/Locations")
  public ResponseEntity<?> getAllLocation() {
    return ResponseEntity.ok().body(locationService.getAll());
  }

  @GetMapping("/jobs/{jobsId}")
  public ResponseEntity<?> getJobById(@Valid @PathVariable Long jobsId) {
    return ResponseEntity.ok().body(jobService.getById(jobsId));
  }
}
