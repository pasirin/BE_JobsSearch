package com.example.JobsSearch.service.impl;

import com.example.JobsSearch.model.util.Location;
import com.example.JobsSearch.repository.util.LocationRepository;
import lombok.SneakyThrows;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Collection;
import java.util.Optional;
import java.util.Scanner;

@Service
public class LocationService {
  private static final Logger logger = LoggerFactory.getLogger(LocationService.class);
  @Autowired LocationRepository locationRepository;

  @EventListener
  @SneakyThrows
  public void initLocation(ApplicationReadyEvent event) {
    ClassLoader classLoader = getClass().getClassLoader();
    InputStream in = classLoader.getResourceAsStream("src/main/resources/Location.txt");
    if (in == null) {
      logger.error("Could not find location text file at: src/main/resources/Location.txt");
      return;
    }
    Scanner scanner = new Scanner(new InputStreamReader(in));
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
