package com.example.JobsSearch.model;

import com.example.JobsSearch.model.util.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;

import javax.persistence.*;
import java.sql.Date;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Entity
@Table(name = "jobs")
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

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "locationId")
    private Location location;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "salariesId")
    private Salary salary;

    @OneToMany(fetch = FetchType.LAZY)
    @JoinColumn(name = "job_id")
    private List<WorkTime> workingHours;

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


    @OneToMany(mappedBy = "primaryKey.job")
    private Set<History> histories = new HashSet<>();

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "photoGalleryId")
    private PhotoGallery photoGallery;

    public Set<History> getHistories() {
        return histories;
    }

    public void setHistories(Set<History> histories) {
        this.histories = histories;
    }

    @OneToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @PrimaryKeyJoinColumn(name = "interviewId")
    private Interview interview;

    private String productCode;

    public Job(Long id, HiringOrganization organization, LocalDateTime createdAt, LocalDateTime updatedAt, LocalDateTime opensAt, LocalDateTime expiresAt, Image mainImage, String title, String jobTitleCatchPhrase, Location location, Salary salary, List<WorkTime> workingHours, List<SearchLabel> searchLabels, WebApplication webApplication, List<PostScript> postScripts, String catchText, String leadText, List<Image> subImages, List<Property> properties, CompanySurvey companySurvey, Barometer barometer, Set<History> histories, PhotoGallery photoGallery, Interview interview, String productCode) {
        this.id = id;
        this.organization = organization;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
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
        this.histories = histories;
        this.photoGallery = photoGallery;
        this.interview = interview;
        this.productCode = productCode;
    }

    public Job() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public HiringOrganization getOrganization() {
        return organization;
    }

    public void setOrganization(HiringOrganization organization) {
        this.organization = organization;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(LocalDateTime updatedAt) {
        this.updatedAt = updatedAt;
    }

    public LocalDateTime getOpensAt() {
        return opensAt;
    }

    public void setOpensAt(LocalDateTime opensAt) {
        this.opensAt = opensAt;
    }

    public LocalDateTime getExpiresAt() {
        return expiresAt;
    }

    public void setExpiresAt(LocalDateTime expiresAt) {
        this.expiresAt = expiresAt;
    }

    public Image getMainImage() {
        return mainImage;
    }

    public void setMainImage(Image mainImage) {
        this.mainImage = mainImage;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getJobTitleCatchPhrase() {
        return jobTitleCatchPhrase;
    }

    public void setJobTitleCatchPhrase(String jobTitleCatchPhrase) {
        this.jobTitleCatchPhrase = jobTitleCatchPhrase;
    }

    public Location getLocation() {
        return location;
    }

    public void setLocation(Location location) {
        this.location = location;
    }

    public Salary getSalary() {
        return salary;
    }

    public void setSalary(Salary salary) {
        this.salary = salary;
    }

    public List<WorkTime> getWorkingHours() {
        return workingHours;
    }

    public void setWorkingHours(List<WorkTime> workingHours) {
        this.workingHours = workingHours;
    }

    public List<SearchLabel> getSearchLabels() {
        return searchLabels;
    }

    public void setSearchLabels(List<SearchLabel> searchLabels) {
        this.searchLabels = searchLabels;
    }

    public WebApplication getWebApplication() {
        return webApplication;
    }

    public void setWebApplication(WebApplication webApplication) {
        this.webApplication = webApplication;
    }

    public List<PostScript> getPostScripts() {
        return postScripts;
    }

    public void setPostScripts(List<PostScript> postScripts) {
        this.postScripts = postScripts;
    }

    public String getCatchText() {
        return catchText;
    }

    public void setCatchText(String catchText) {
        this.catchText = catchText;
    }

    public String getLeadText() {
        return leadText;
    }

    public void setLeadText(String leadText) {
        this.leadText = leadText;
    }

    public List<Image> getSubImages() {
        return subImages;
    }

    public void setSubImages(List<Image> subImages) {
        this.subImages = subImages;
    }

    public List<Property> getProperties() {
        return properties;
    }

    public void setProperties(List<Property> properties) {
        this.properties = properties;
    }

    public CompanySurvey getCompanySurvey() {
        return companySurvey;
    }

    public void setCompanySurvey(CompanySurvey companySurvey) {
        this.companySurvey = companySurvey;
    }

    public Barometer getBarometer() {
        return barometer;
    }

    public void setBarometer(Barometer barometer) {
        this.barometer = barometer;
    }

    public PhotoGallery getPhotoGallery() {
        return photoGallery;
    }

    public void setPhotoGallery(PhotoGallery photoGallery) {
        this.photoGallery = photoGallery;
    }

    public Interview getInterview() {
        return interview;
    }

    public void setInterview(Interview interview) {
        this.interview = interview;
    }

    public String getProductCode() {
        return productCode;
    }

    public void setProductCode(String productCode) {
        this.productCode = productCode;
    }
}
