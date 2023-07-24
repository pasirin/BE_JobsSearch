package com.example.JobsSearch.service.impl;

import com.example.JobsSearch.model.util.Location;
import com.example.JobsSearch.repository.util.LocationRepository;
import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Service;

import java.io.File;
import java.util.Collection;
import java.util.Optional;
import java.util.Scanner;

@Service
public class LocationService {
  @Autowired LocationRepository locationRepository;

  @EventListener
  @SneakyThrows
  public void initLocation(ApplicationReadyEvent event) {
    File file = new File("./src/main/resources/Location.txt");
    Scanner scanner = new Scanner(file);
    while (scanner.hasNextLine()) {
      String temp = scanner.nextLine();
      if (!locationRepository.existsByCity(temp)) {
        Location location = new Location(temp);
        locationRepository.save(location);
      }
    }
  }

  public Collection<Location> getAll() {
    return locationRepository.findAll();
  }

  public Optional<Location> getById(Long id) {
    return locationRepository.findById(id);
  }
}
