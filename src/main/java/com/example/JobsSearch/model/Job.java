package com.example.JobsSearch.model;

import com.example.JobsSearch.model.util.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
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
  private HiringOrganization organization;

  @CreatedDate private LocalDateTime createdAt;

  @LastModifiedDate private LocalDateTime updatedAt;

  private LocalDateTime opensAt;

  private LocalDateTime expiresAt;

  @OneToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
  @JoinColumn(name = "mainImageId")
  private Image mainImage;

  private String title;

  private String jobTitleCatchPhrase;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "locationId")
  private Location location;

  @OneToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
  @JoinColumn(name = "salariesId")
  private Salary salary;

  @OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
  @JoinColumn(name = "job_id")
  private List<WorkingHour> workingHours;

  @ManyToMany(fetch = FetchType.LAZY)
  @JoinColumn(name = "job_id")
  private List<SearchLabel> searchLabels;

  @OneToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true, mappedBy = "job")
  private WebApplication webApplication;

  @OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
  @JoinColumn(name = "job_id")
  private List<PostScript> postScripts;

  private String catchText;
  private String leadText;

  @OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
  @JoinColumn(name = "job_id")
  private List<Image> subImages;

  @OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
  @JoinColumn(name = "job_id")
  private List<Property> properties;

  @OneToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true, mappedBy = "job")
  private CompanySurvey companySurvey;

  @OneToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true, mappedBy = "job")
  private Barometer barometer;

  @OneToOne(fetch = FetchType.LAZY, orphanRemoval = true)
  @JoinColumn(name = "photoGalleryId")
  private PhotoGallery photoGallery;

  @OneToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true,mappedBy = "job")
  private Interview interview;

  private String productCode;

  @OneToMany(mappedBy = "primaryKey.job", cascade = CascadeType.ALL)
  private Set<History> histories = new HashSet<>();

  public Job(
      HiringOrganization organization,
      LocalDateTime opensAt,
      LocalDateTime expiresAt,
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
      LocalDateTime opensAt,
      LocalDateTime expiresAt,
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
