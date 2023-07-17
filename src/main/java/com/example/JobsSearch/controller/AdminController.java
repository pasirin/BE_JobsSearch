package com.example.JobsSearch.controller;

import com.example.JobsSearch.payload.Request.NewsRequest;
import com.example.JobsSearch.payload.Response.ResponseObject;
import com.example.JobsSearch.repository.SeekerRepository;
import com.example.JobsSearch.service.impl.NewsService;
import com.example.JobsSearch.service.impl.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping("/test/admin")
public class AdminController {
  @Autowired NewsService newsService;

  @Autowired UserService userService;

  @Autowired
  SeekerRepository seekerRepository;

  // Account Managing Section
  @GetMapping("/users")
  public ResponseEntity<?> getAllUser() {
    return ResponseEntity.ok().body(userService.getAll());
  }

  @GetMapping("/users/search")
  public ResponseEntity<?> findByUsername(@Valid @RequestParam String s) {
    return ResponseEntity.ok().body(userService.getByName(s));
  }

  @GetMapping("/users/{id}")
  public ResponseEntity<?> findById(@Valid @PathVariable Long id) {
    return ResponseEntity.ok().body(userService.getById(id));
  }

  @DeleteMapping("/users/{id}")
  public ResponseEntity<?> deleteById(@Valid @PathVariable Long id) {
    ResponseObject output = userService.delete(id);
    return output.getStatus()
        ? ResponseEntity.ok().build()
        : ResponseEntity.badRequest().body(output.getMessage());
  }

  // News Section
  @PostMapping("/news/add")
  public ResponseEntity<?> createNews(@Valid @RequestBody NewsRequest newsRequest) {
    return ResponseEntity.ok().body(newsService.create(newsRequest));
  }

  @GetMapping("/news/{id}")
  public ResponseEntity<?> getNews(@Valid @PathVariable Long id) {
    return ResponseEntity.ok().body(newsService.getById(id));
  }

  @DeleteMapping("/news/{id}")
  public ResponseEntity<?> deleteNews(@Valid @PathVariable Long id) {
    ResponseObject output = newsService.delete(id);
    return output.getStatus()
        ? ResponseEntity.ok().build()
        : ResponseEntity.badRequest().body(output.getMessage());
  }

  @PostMapping("/news/{id}")
  public ResponseEntity<?> changeNews(
      @Valid @PathVariable Long id, @RequestBody NewsRequest newsRequest) {
    ResponseObject output = newsService.update(id, newsRequest);
    return output.getStatus()
        ? ResponseEntity.ok().build()
        : ResponseEntity.badRequest().body(output.getMessage());
  }
}
