package com.example.JobsSearch.payload.Request;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import java.sql.Date;

@Data
public class SeekerUpdateRequest {
  private String name;
  private String phone_number;
  private Date dob;
  private String address;
  private String website;
  private String education;
  private String experience;
  private String skills;
  private String achievements;
  private String other_details;
  private String profileImageUrl;
}
