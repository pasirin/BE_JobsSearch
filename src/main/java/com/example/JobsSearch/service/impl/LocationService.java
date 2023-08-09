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

import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.*;

@Service
public class LocationService {
  @Autowired LocationRepository locationRepository;

  @EventListener
  @SneakyThrows
  public void initLocation(ApplicationReadyEvent event) {
    List<String> locations =
        Arrays.asList(
            "Hà Nội",
            "Hà Giang",
            "Cao Bằng",
            "Bắc Kạn",
            "Tuyên Quang",
            "Lào Cai",
            "Điện Biên",
            "Lai Châu",
            "Sơn La",
            "Yên Bái",
            "Hoà Bình",
            "Thái Nguyên",
            "Lạng Sơn",
            "Quảng Ninh",
            "Bắc Giang",
            "Phú Thọ",
            "Vĩnh Phúc",
            "Bắc Ninh",
            "Hải Dương",
            "Hải Phòng",
            "Hưng Yên",
            "Thái Bình",
            "Hà Nam",
            "Nam Định",
            "Ninh Bình",
            "Thanh Hóa",
            "Nghệ An",
            "Hà Tĩnh",
            "Quảng Bình",
            "Quảng Trị",
            "Thừa Thiên Huế",
            "Đà Nẵng",
            "Quảng Nam",
            "Quảng Ngãi",
            "Bình Định",
            "Phú Yên",
            "Khánh Hòa",
            "Ninh Thuận",
            "Bình Thuận",
            "Kon Tum",
            "Gia Lai",
            "Đắk Lắk",
            "Đắk Nông",
            "Lâm Đồng",
            "Bình Phước",
            "Tây Ninh",
            "Bình Dương",
            "Đồng Nai",
            "Bà Rịa - Vũng Tàu",
            "Hồ Chí Minh",
            "Long An",
            "Tiền Giang",
            "Bến Tre",
            "Trà Vinh",
            "Vĩnh Long",
            "Đồng Tháp",
            "An Giang",
            "Kiên Giang",
            "Cần Thơ",
            "Hậu Giang",
            "Sóc Trăng",
            "Bạc Liêu",
            "Cà Mau");
    for (String locationString : locations) {
      if (!locationRepository.existsByCity(locationString)) {
        Location location = new Location(locationString);
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
