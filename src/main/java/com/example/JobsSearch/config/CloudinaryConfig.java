package com.example.JobsSearch.config;

import com.cloudinary.Cloudinary;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.HashMap;
import java.util.Map;

@Configuration
public class CloudinaryConfig {
  @Value("${demo.app.cloudName}")
  private String cloudName;

  @Value("${demo.app.cloudKey}")
  private String cloudKey;

  @Value("${demo.app.cloudSecret}")
  private String cloudSecret;

  @Bean
  public Cloudinary cloudinary() {
    Map<String, String> config = new HashMap<>();
    config.put("cloud_name", cloudName);
    config.put("api_key", cloudKey);
    config.put("api_secret", cloudSecret);

    return new Cloudinary(config);
  }
}
