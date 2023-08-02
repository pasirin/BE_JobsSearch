package com.example.JobsSearch.payload.Request;

import lombok.Getter;
import lombok.Setter;
import java.sql.Date;

public class SeekerUpdateRequest {
  @Getter @Setter private String name;
  @Getter @Setter private String phoneNumber;
  @Getter @Setter private Date dob;
  @Getter @Setter private String address;
  @Getter @Setter private String website;
  @Getter @Setter private String education;
  @Getter @Setter private String experience;
  @Getter @Setter private String skills;
  @Getter @Setter private String achievements;
  @Getter @Setter private String other_details;
  @Getter @Setter private String profileImageUrl;
}
