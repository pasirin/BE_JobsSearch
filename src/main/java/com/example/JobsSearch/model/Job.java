package com.example.JobsSearch.model;

import com.example.JobsSearch.model.util.*;
import lombok.Data;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Data
@Entity
@Table(name = "jobs")
@EntityListeners(AuditingEntityListener.class)
public class Job {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "organizationId")
    private HiringOrganization organization;

    @CreatedDate
    private LocalDateTime createdAt;

    @LastModifiedDate
    private LocalDateTime updatedAt;

    private LocalDateTime opensAt;

    private LocalDateTime expiresAt;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "mainImageId")
    private Image mainImage;

    private String title;

    private String jobTitleCatchPhrase;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "locationId")
    private Location location;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "salariesId")
    private Salary salary;

    @OneToMany(fetch = FetchType.LAZY)
    @JoinColumn(name = "job_id")
    private List<WorkingHour> workingHours;

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinColumn(name = "job_id")
    private List<SearchLabel> searchLabels;


    @OneToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @PrimaryKeyJoinColumn(name = "webApplicationId")
    private WebApplication webApplication;

    @OneToMany(fetch = FetchType.LAZY)
    @JoinColumn(name = "job_id")
    private List<PostScript> postScripts;

    private String catchText;
    private String leadText;

    @OneToMany(fetch = FetchType.LAZY)
    @JoinColumn(name = "job_id")
    private List<Image> subImages;

    @OneToMany(fetch = FetchType.LAZY)
    @JoinColumn(name = "job_id")
    private List<Property> properties;

    @OneToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @PrimaryKeyJoinColumn(name = "companySurveyId")
    private CompanySurvey companySurvey;

    @OneToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @PrimaryKeyJoinColumn(name = "barometerId")
    private Barometer barometer;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "photoGalleryId")
    private PhotoGallery photoGallery;

    @OneToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @PrimaryKeyJoinColumn(name = "interviewId")
    private Interview interview;

    private String productCode;

    @OneToMany(mappedBy = "primaryKey.job")
    private Set<History> histories = new HashSet<>();


}
