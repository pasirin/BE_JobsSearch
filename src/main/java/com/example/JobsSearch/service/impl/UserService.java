package com.example.JobsSearch.service.impl;

import com.example.JobsSearch.model.HiringOrganization;
import com.example.JobsSearch.model.Seeker;
import com.example.JobsSearch.model.User;
import com.example.JobsSearch.model.util.EStatus;
import com.example.JobsSearch.payload.Response.ResponseObject;
import com.example.JobsSearch.repository.HiringOrganizationRepository;
import com.example.JobsSearch.repository.SeekerRepository;
import com.example.JobsSearch.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class UserService {
  @Autowired UserRepository userRepository;

  @Autowired SeekerRepository seekerRepository;

  @Autowired HiringOrganizationRepository hiringOrganizationRepository;

  public Collection<User> getAll() {
    return userRepository.findAll().stream()
        .sorted(Comparator.comparing(User::getId))
        .collect(Collectors.toList());
  }

  public Collection<User> searchQuery(String email, String username, EStatus status) {
    return userRepository.findByEmailLikeAndUsernameLikeAndStatus(
        "%" + (email == null ? "" : email) + "%",
        "%" + (username == null ? "" : username) + "%",
        status);
  }

  public ResponseObject getById(Long id) {
    if (userRepository.findById(id).isEmpty()) {
      return ResponseObject.message("There Aren't any user with the requested Id");
    }
    return ResponseObject.ok().setData(userRepository.findById(id).get());
  }

  public ResponseObject getByUserId(Long id) {
    if (!userRepository.existsById(id)) {
      return ResponseObject.message("There Aren't any user with the requested id");
    }
    if (seekerRepository.findByUserId(id).isPresent()) {
      return ResponseObject.ok().setData(seekerRepository.findByUserId(id));
    } else if (hiringOrganizationRepository.findByUserId(id).isPresent()) {
      return ResponseObject.ok().setData(hiringOrganizationRepository.findByUserId(id));
    } else {
      return ResponseObject.ok().setData(userRepository.findById(id));
    }
  }

  public ResponseObject delete(Long id) {
    if (userRepository.findById(id).isEmpty()) {
      return ResponseObject.message("There Aren't any user with the requested Id");
    }
    if (seekerRepository.findByUserId(id).isEmpty()) {
      if (hiringOrganizationRepository.findByUserId(id).isEmpty()) {
        User user = userRepository.findById(id).get();
        userRepository.delete(user);
      } else {
        HiringOrganization hiringOrganization = hiringOrganizationRepository.findByUserId(id).get();
        hiringOrganizationRepository.delete(hiringOrganization);
      }
    } else {
      Seeker seeker = seekerRepository.findByUserId(id).get();
      seekerRepository.delete(seeker);
    }
    return ResponseObject.ok();
  }

  public ResponseObject setStatus(Long id, String status) {
    if (userRepository.findById(id).isEmpty()) {
      return ResponseObject.message("There Aren't any user with the requested Id");
    }
    User user = userRepository.findById(id).get();
    switch (status) {
      case "active":
        user.setStatus(EStatus.ACTIVE);
        break;
      case "disable":
        user.setStatus(EStatus.DISABLE);
        break;
      default:
        return ResponseObject.message("There aren't any account status that matches your request");
    }
    userRepository.save(user);
    return ResponseObject.ok();
  }
}
