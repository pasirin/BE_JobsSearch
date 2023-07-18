package com.example.JobsSearch.service.impl;

import com.example.JobsSearch.model.Seeker;
import com.example.JobsSearch.payload.Response.ResponseObject;
import com.example.JobsSearch.repository.SeekerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.parameters.P;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.OptionalInt;

@Service
public class SeekerService {
  @Autowired SeekerRepository seekerRepository;

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
}
