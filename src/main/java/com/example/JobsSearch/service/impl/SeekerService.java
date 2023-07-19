package com.example.JobsSearch.service.impl;

import com.example.JobsSearch.model.Seeker;
import com.example.JobsSearch.payload.Request.SeekerUpdateRequest;
import com.example.JobsSearch.payload.Response.ResponseObject;
import com.example.JobsSearch.repository.SeekerRepository;
import com.example.JobsSearch.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Collection;

@Service
public class SeekerService {
  @Autowired SeekerRepository seekerRepository;

  @Autowired
  UserRepository userRepository;
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

  public ResponseObject delete(Long id) {
    if (!seekerRepository.existsById(id)) {
      return ResponseObject.message("There Aren't any Seeker with the provided Id");
    }
    seekerRepository.deleteById(id);
    return ResponseObject.ok();
  }

  public ResponseObject update(
      Long id, SeekerUpdateRequest seekerUpdateRequest, MultipartFile image) {
    if (seekerRepository.findByUserId(id).isEmpty()) {
      return ResponseObject.message("There Aren't any Seeker with the provided Id:" + id);
    }
    Seeker seeker = seekerRepository.findByUserId(id).get();
    seeker.setName(seekerUpdateRequest.getName());
    seeker.setPhone_number(seekerUpdateRequest.getPhone_number());
    seeker.setDob(seekerUpdateRequest.getDob());
    seeker.setAddress(seekerUpdateRequest.getAddress());
    seeker.setWebsite(seekerUpdateRequest.getWebsite());
    seeker.setEducation(seekerUpdateRequest.getEducation());
    seeker.setExperience(seekerUpdateRequest.getExperience());
    seeker.setSkills(seekerUpdateRequest.getSkills());
    seeker.setAchievements(seekerUpdateRequest.getAchievements());
    seeker.setOther_details(seekerUpdateRequest.getOther_details());
    if(!seeker.getPhoto().isEmpty()) {
      try {
        String result = imageUploadService.deleteImage(seeker.getPhoto());
      } catch (IOException e) {
        throw new RuntimeException("Failed to delete the previous profile photo of user id: " + seeker.getId());
      }
    }
    String profilePictureUrl;
    if (image != null) {
      try {
        profilePictureUrl = imageUploadService.uploadImage(image);
      } catch (IOException e) {
        return ResponseObject.message(e.toString());
      }
      seeker.setPhoto(profilePictureUrl);
    }
    seekerRepository.save(seeker);
    return ResponseObject.ok();
  }
}
