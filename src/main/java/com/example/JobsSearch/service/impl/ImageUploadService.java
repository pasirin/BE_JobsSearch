package com.example.JobsSearch.service.impl;

import com.cloudinary.Cloudinary;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Collections;
import java.util.Map;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ImageUploadService {
  @Autowired private Cloudinary cloudinary;

  public String uploadImage(MultipartFile multipartFile) throws IOException {
    return cloudinary
        .uploader()
        .upload(multipartFile.getBytes(), Map.of("public_id", UUID.randomUUID().toString()))
        .get("url")
        .toString();
  }

  public String deleteImage(String url) throws IOException {
    return cloudinary
        .uploader()
        .destroy(url.substring(url.length() - 40, url.length() - 4), Collections.emptyMap())
        .get("result")
        .toString();
  }
}
