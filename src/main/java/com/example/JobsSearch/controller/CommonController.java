package com.example.JobsSearch.controller;

import com.example.JobsSearch.service.impl.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.Valid;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@RestController
public class CommonController {
  private static final Logger logger = LoggerFactory.getLogger(CommonController.class);
  @Autowired NewsService newsService;

  @Autowired SearchLabelService searchLabelService;

  @Autowired LocationService locationService;

  @Autowired ImageUploadService imageUploadService;

  @Autowired JobService jobService;

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

  @PostMapping("/image/upload")
  public ResponseEntity<?> uploadImage(@Valid @RequestPart MultipartFile image) {
    String url;
    try {
      url = imageUploadService.uploadImage(image);
    } catch (IOException e) {
      return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
          .body("Unable To Upload Images");
    }
    return ResponseEntity.ok().body(url);
  }

  @PostMapping("/image/uploads")
  public ResponseEntity<?> uploadMultipleImages(@Valid @RequestPart List<MultipartFile> images) {
    List<String> urls = new ArrayList<>();
    images.forEach(
        image -> {
          String url = null;
          try {
            url = imageUploadService.uploadImage(image);
          } catch (IOException e) {
            logger.error(String.valueOf(e));
          }
          urls.add(url);
        });
    return ResponseEntity.ok().body(urls);
  }
}
