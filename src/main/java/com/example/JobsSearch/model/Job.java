package com.example.JobsSearch.model;

import com.example.JobsSearch.model.util.*;
import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Data
@NoArgsConstructor
@Entity
@Table(name = "jobs")
@EntityListeners(AuditingEntityListener.class)
public class Job {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(nullable = false, unique = true)
  private Long id;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "organizationId")
  @JsonIgnore
  private HiringOrganization organization;

  @CreatedDate private LocalDateTime createdAt;

  @LastModifiedDate private LocalDateTime updatedAt;

  private LocalDate opensAt;

  private LocalDate expiresAt;

  @OneToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
  @JoinColumn(name = "mainImageId")
  @JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
  private Image mainImage;

  private String title;

  private String jobTitleCatchPhrase;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "locationId")
  @JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
  private Location location;

  @OneToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
  @JoinColumn(name = "salariesId")
  @JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
  private Salary salary;

  @OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
  @JoinColumn(name = "job_id")
  @JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
  private List<WorkingHour> workingHours;

  @ManyToMany(fetch = FetchType.LAZY)
  @JoinColumn(name = "job_id")
  @JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
  private List<SearchLabel> searchLabels;

  @OneToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true, mappedBy = "job")
  @JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
  private WebApplication webApplication;

  @OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
  @JoinColumn(name = "job_id")
  @JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
  private List<PostScript> postScripts;

  private String catchText;
  private String leadText;

  @OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
  @JoinColumn(name = "job_id")
  @JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
  private List<Image> subImages;

  @OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
  @JoinColumn(name = "job_id")
  @JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
  private List<Property> properties;

  @OneToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true, mappedBy = "job")
  @JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
  private CompanySurvey companySurvey;

  @OneToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true, mappedBy = "job")
  @JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
  private Barometer barometer;

  @OneToOne(fetch = FetchType.LAZY, orphanRemoval = true)
  @JoinColumn(name = "photoGalleryId")
  @JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
  private PhotoGallery photoGallery;

  @OneToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true,mappedBy = "job")
  @JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
  private Interview interview;

  private String productCode;

  @OneToMany(mappedBy = "primaryKey.job", cascade = CascadeType.ALL)
  private Set<History> histories = new HashSet<>();

  public Job(
      HiringOrganization organization,
      LocalDate opensAt,
      LocalDate expiresAt,
      Image mainImage,
      String title,
      String jobTitleCatchPhrase,
      Location location,
      Salary salary,
      List<WorkingHour> workingHours,
      List<SearchLabel> searchLabels,
      List<PostScript> postScripts,
      String catchText,
      String leadText,
      List<Image> subImages,
      List<Property> properties,
      PhotoGallery photoGallery,
      String productCode) {
    this.organization = organization;
    this.opensAt = opensAt;
    this.expiresAt = expiresAt;
    this.mainImage = mainImage;
    this.title = title;
    this.jobTitleCatchPhrase = jobTitleCatchPhrase;
    this.location = location;
    this.salary = salary;
    this.workingHours = workingHours;
    this.searchLabels = searchLabels;
    this.postScripts = postScripts;
    this.catchText = catchText;
    this.leadText = leadText;
    this.subImages = subImages;
    this.properties = properties;
    this.photoGallery = photoGallery;
    this.productCode = productCode;
  }

  public Job(
      HiringOrganization organization,
      LocalDate opensAt,
      LocalDate expiresAt,
      Image mainImage,
      String title,
      String jobTitleCatchPhrase,
      Location location,
      Salary salary,
      List<WorkingHour> workingHours,
      List<SearchLabel> searchLabels,
      WebApplication webApplication,
      List<PostScript> postScripts,
      String catchText,
      String leadText,
      List<Image> subImages,
      List<Property> properties,
      CompanySurvey companySurvey,
      Barometer barometer,
      PhotoGallery photoGallery,
      Interview interview,
      String productCode) {
    this.organization = organization;
    this.opensAt = opensAt;
    this.expiresAt = expiresAt;
    this.mainImage = mainImage;
    this.title = title;
    this.jobTitleCatchPhrase = jobTitleCatchPhrase;
    this.location = location;
    this.salary = salary;
    this.workingHours = workingHours;
    this.searchLabels = searchLabels;
    this.webApplication = webApplication;
    this.postScripts = postScripts;
    this.catchText = catchText;
    this.leadText = leadText;
    this.subImages = subImages;
    this.properties = properties;
    this.companySurvey = companySurvey;
    this.barometer = barometer;
    this.photoGallery = photoGallery;
    this.interview = interview;
    this.productCode = productCode;
  }
}
