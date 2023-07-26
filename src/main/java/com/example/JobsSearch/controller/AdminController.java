package com.example.JobsSearch.controller;

import com.example.JobsSearch.model.util.EStatus;
import com.example.JobsSearch.payload.Request.NewsRequest;
import com.example.JobsSearch.payload.Request.SearchLabelRequest;
import com.example.JobsSearch.payload.Response.ResponseObject;
import com.example.JobsSearch.service.impl.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.parameters.P;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.Email;

@RestController
@RequestMapping("/admin")
@PreAuthorize("hasRole('ADMIN')")
public class AdminController {
  @Autowired NewsService newsService;

  @Autowired UserService userService;

  @Autowired SeekerService seekerService;

  @Autowired HiringOrganizationService hiringOrganizationService;

  @Autowired SearchLabelService searchLabelService;

  // Seeker Managing Section

  @GetMapping("/seekers")
  public ResponseEntity<?> getAllSeeker() {
    return ResponseEntity.ok().body(seekerService.getAllSeekers());
  }

  @GetMapping("/seekers/q")
  public ResponseEntity<?> searchSeeker(@Valid @RequestParam String s) {
    return ResponseEntity.ok().body(seekerService.getByName(s));
  }

  @GetMapping("/seeker/{id}")
  public ResponseEntity<?> getSeekerById(@Valid @PathVariable Long id) {
    ResponseObject output = seekerService.getById(id);
    return output.getStatus()
        ? ResponseEntity.ok().body(output.getData())
        : ResponseEntity.badRequest().body(output.getMessage());
  }

  @DeleteMapping("/seeker/{id}")
  public ResponseEntity<?> deleteSeekerById(@Valid @PathVariable Long id) {
    ResponseObject output = seekerService.delete(id);
    return output.getStatus()
        ? ResponseEntity.ok().build()
        : ResponseEntity.badRequest().body(output.getMessage());
  }

  /**
   * HR management section
   *
   * @return
   */
  @GetMapping("/organizations")
  public ResponseEntity<?> getAllHr() {
    return ResponseEntity.ok().body(hiringOrganizationService.getAll());
  }

  @GetMapping("/organizations/q")
  public ResponseEntity<?> searchHr(
      @Valid @RequestParam(required = false) String name,
      @Valid @RequestParam(required = false) @Email String email,
      @Valid @RequestParam(required = false) String phoneNumber) {
    return ResponseEntity.ok()
        .body(hiringOrganizationService.searchQuery(email, name, phoneNumber));
  }

  @GetMapping("/organizations/organizationId}")
  public ResponseEntity<?> getById(@Valid @PathVariable Long organizationId) {
    ResponseObject output = hiringOrganizationService.getById(organizationId);
    return output.getStatus()
        ? ResponseEntity.ok().body(output.getData())
        : ResponseEntity.badRequest().body(output.getMessage());
  }

  @DeleteMapping("/organizations/{organizationId}")
  public ResponseEntity<?> delete(@Valid @PathVariable Long organizationId) {
    ResponseObject output = hiringOrganizationService.delete(organizationId);
    return output.getStatus()
        ? ResponseEntity.ok().build()
        : ResponseEntity.badRequest().body(output.getMessage());
  }

  // User Managing Section
  @GetMapping("/users")
  public ResponseEntity<?> getAllUser() {
    return ResponseEntity.ok().body(userService.getAll());
  }

  @GetMapping("/users/q")
  public ResponseEntity<?> searchUsername(@Valid @RequestParam String s) {
    return ResponseEntity.ok().body(userService.getByName(s));
  }

  @GetMapping("/users/{id}")
  public ResponseEntity<?> findUserById(@Valid @PathVariable Long id) {
    ResponseObject output = userService.getById(id);
    return output.getStatus()
        ? ResponseEntity.ok().body(output.getData())
        : ResponseEntity.badRequest().body(output.getMessage());
  }

  @DeleteMapping("/users/{id}")
  public ResponseEntity<?> deleteUserById(@Valid @PathVariable Long id) {
    ResponseObject output = userService.delete(id);
    return output.getStatus()
        ? ResponseEntity.ok().build()
        : ResponseEntity.badRequest().body(output.getMessage());
  }

  @PostMapping("/users/{id}/status")
  public ResponseEntity<?> changeUserStatus(@PathVariable Long id, @Valid EStatus eStatus) {
    ResponseObject output = userService.setStatus(id, eStatus);
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

  @PutMapping("/news/{id}")
  public ResponseEntity<?> changeNews(
      @Valid @PathVariable Long id, @RequestBody NewsRequest newsRequest) {
    ResponseObject output = newsService.update(id, newsRequest);
    return output.getStatus()
        ? ResponseEntity.ok().build()
        : ResponseEntity.badRequest().body(output.getMessage());
  }

  // Search label section
  @PostMapping("/tag-search/add")
  public ResponseEntity<?> createSearchLabel(SearchLabelRequest searchLabelRequest) {
    ResponseObject output = searchLabelService.create(searchLabelRequest);
    return output.getStatus()
        ? ResponseEntity.ok().build()
        : ResponseEntity.badRequest().body(output.getMessage());
  }

  @GetMapping("/tag-search/{id}")
  public ResponseEntity<?> getLabelById(@Valid @PathVariable Long id) {
    return ResponseEntity.ok().body(searchLabelService.getById(id));
  }

  @DeleteMapping("/tag-search/{id}")
  public ResponseEntity<?> deleteLabelById(@Valid @PathVariable Long id) {
    ResponseObject output = searchLabelService.delete(id);
    return output.getStatus()
        ? ResponseEntity.ok().build()
        : ResponseEntity.badRequest().body(output.getMessage());
  }

  @PutMapping("/tag-search/{id}")
  public ResponseEntity<?> updateSearchLabel(
      @Valid @PathVariable Long id, @RequestBody SearchLabelRequest searchLabelRequest) {
    ResponseObject output = searchLabelService.update(id, searchLabelRequest);
    return output.getStatus()
        ? ResponseEntity.ok().build()
        : ResponseEntity.badRequest().body(output.getMessage());
  }
}
