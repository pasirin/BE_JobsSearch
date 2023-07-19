package com.example.JobsSearch.controller;

import com.example.JobsSearch.service.ImageUploadService;
import com.example.JobsSearch.service.impl.NewsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@RestController
public class CommonController {
  @Autowired NewsService newsService;

  @Autowired
  ImageUploadService imageUploadService;

  @GetMapping("/news")
  public ResponseEntity<?> getAllNews() {
    return ResponseEntity.ok().body(newsService.getAll());
  }
}
