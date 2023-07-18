package com.example.JobsSearch.service.impl;

import com.example.JobsSearch.model.HiringOrganization;
import com.example.JobsSearch.model.Seeker;
import com.example.JobsSearch.model.User;
import com.example.JobsSearch.payload.Response.ResponseObject;
import com.example.JobsSearch.repository.HiringOrganizationRepository;
import com.example.JobsSearch.repository.SeekerRepository;
import com.example.JobsSearch.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import javax.swing.text.html.Option;
import java.util.Collection;
import java.util.List;
import java.util.Optional;

@Service
public class UserService {
  @Autowired UserRepository userRepository;

  @Autowired SeekerRepository seekerRepository;

  @Autowired HiringOrganizationRepository hiringOrganizationRepository;

  public Collection<User> getAll() {
    return userRepository.findAll();
  }

  public List<User> getByName(String name) {
    return userRepository.findByUsernameLike("%" + name + "%");
  }

  public ResponseObject getById(Long id) {
    if(userRepository.findById(id).isEmpty()) {
      return ResponseObject.message("There Aren't any user with the requested Id");
    }
    return ResponseObject.ok().setData(userRepository.findById(id).get());
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
}
