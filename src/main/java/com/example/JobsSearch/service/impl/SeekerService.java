package com.example.JobsSearch.service.impl;

import com.example.JobsSearch.model.Seeker;
import com.example.JobsSearch.payload.Request.SeekerUpdateRequest;
import com.example.JobsSearch.payload.Response.ResponseObject;
import com.example.JobsSearch.repository.SeekerRepository;
import com.example.JobsSearch.repository.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.Collection;

@Service
public class SeekerService {
  private static final Logger logger = LoggerFactory.getLogger(SeekerService.class);

  @Autowired SeekerRepository seekerRepository;

  @Autowired UserRepository userRepository;
  @Autowired ImageUploadService imageUploadService;

  public ResponseObject getProfile(Long id) {
    if (seekerRepository.findByUserId(id).isEmpty()) {
      return ResponseObject.message("There Aren't any Seeker with the provided Id");
    }
    return ResponseObject.ok().setData(seekerRepository.findByUserId(id));
  }

  public ResponseObject getById(Long id) {
    if (seekerRepository.findById(id).isEmpty()) {
      return ResponseObject.message("There Aren't any Seeker with the provided Id");
    }
    return ResponseObject.ok().setData(seekerRepository.findById(id).get());
  }

  public Collection<Seeker> getAllSeekers() {
    return seekerRepository.findAll();
  }

  public Collection<Seeker> getByName(String name) {
    return seekerRepository.findByNameLike("%" + name + "%");
  }

  public Collection<Seeker> searchQuery(String email, String name, String phoneNumber) {
    return seekerRepository.findByUserEmailLikeAndNameLikeAndPhoneNumberLike(
        "%" + (email == null ? "" : email) + "%",
        "%" + (name == null ? "" : name) + "%",
        "%" + (phoneNumber == null ? "" : phoneNumber) + "%");
  }

  public ResponseObject delete(Long id) {
    if (!seekerRepository.existsById(id)) {
      return ResponseObject.message("There Aren't any Seeker with the provided Id");
    }
    seekerRepository.deleteById(id);
    return ResponseObject.ok();
  }

  public ResponseObject update(Long id, SeekerUpdateRequest seekerUpdateRequest) {
    if (seekerRepository.findByUserId(id).isEmpty()) {
      return ResponseObject.message("There Aren't any Seeker with the provided Id:" + id);
    }
    Seeker seeker = seekerRepository.findByUserId(id).get();
    seeker.setName(seekerUpdateRequest.getName());
    seeker.setPhoneNumber(seekerUpdateRequest.getPhone_number());
    seeker.setDob(seekerUpdateRequest.getDob());
    seeker.setAddress(seekerUpdateRequest.getAddress());
    seeker.setWebsite(seekerUpdateRequest.getWebsite());
    seeker.setEducation(seekerUpdateRequest.getEducation());
    seeker.setExperience(seekerUpdateRequest.getExperience());
    seeker.setSkills(seekerUpdateRequest.getSkills());
    seeker.setAchievements(seekerUpdateRequest.getAchievements());
    seeker.setOther_details(seekerUpdateRequest.getOther_details());
    if (!seeker.getPhoto().isEmpty()) {
      try {
        String result = imageUploadService.deleteImage(seeker.getPhoto());
      } catch (IOException e) {
        logger.error(String.valueOf(e));
      }
    }
    seeker.setPhoto(seekerUpdateRequest.getProfileImageUrl());
    seekerRepository.save(seeker);
    return ResponseObject.ok();
  }
}
