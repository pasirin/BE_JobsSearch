package com.example.JobsSearch.service.impl;

import com.example.JobsSearch.model.*;
import com.example.JobsSearch.model.util.*;
import com.example.JobsSearch.model.util.InteractionType;
import com.example.JobsSearch.payload.Request.JobRequest;
import com.example.JobsSearch.payload.Request.JobSearchRequest;
import com.example.JobsSearch.payload.Response.ResponseObject;
import com.example.JobsSearch.repository.*;
import com.example.JobsSearch.repository.util.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@Service
public class JobService {
  private static final Logger logger = LoggerFactory.getLogger(JobService.class);

  @Autowired JobRepository jobRepository;

  @Autowired HistoryRepository historyRepository;

  @Autowired SeekerRepository seekerRepository;

  @Autowired HiringOrganizationRepository hiringOrganizationRepository;

  @Autowired ImageUploadService imageUploadService;

  @Autowired ImageRepository imageRepository;

  @Autowired LocationRepository locationRepository;

  @Autowired SearchLabelRepository searchLabelRepository;

  @Autowired PhotoGalleryRepository photoGalleryRepository;

  @Autowired PostScriptRepository postScriptRepository;

  @Autowired WorkingHourRepository workingHourRepository;

  @Autowired PropertyRepository propertyRepository;

  @Autowired SalaryRepository salaryRepository;

  public Collection<Job> getAll() {
    return jobRepository.findAll();
  }

  // lấy ra tất cả các job của organization
  public ResponseObject getAllJobsByOrganization(Long userId) {
    if (hiringOrganizationRepository.findByUserId(userId).isEmpty()) {
      return ResponseObject.message("There Aren't any organization with the requested Id");
    }
    Long organizationId = hiringOrganizationRepository.findByUserId(userId).get().getId();
    List<Job> jobList = jobRepository.findByOrganizationId(organizationId);
    if (jobList.isEmpty()) {
      return ResponseObject.message("There are no posts yet");
    }
    return ResponseObject.ok().setData(jobList);
  }

  public Optional<Job> getById(Long jobId) {
    return jobRepository.findById(jobId);
  }

  /** Định nghĩa hàm tìm kiếm cơ bản */
  public Collection<Job> searchJobs(String city, LocalDateTime startTime, LocalDateTime endTime) {
    Specification<Job> specification =
        Specification.where(hasCity(city)).and(hasStartTime(startTime)).and(hasEndTime(endTime));

    return jobRepository.findAll(specification);
  }

  public Collection<Job> searchJobs(Long userId, JobSearchRequest jobSearchRequest) {
    Seeker seeker =
        seekerRepository
            .findByUserId(userId)
            .orElseThrow(() -> new RuntimeException("Seeker not found with id: " + userId));

    Specification<Job> specification =
        Specification.where(hasCity(jobSearchRequest.getLocation1()))
            .and(hasStartTime(LocalDateTime.from(jobSearchRequest.getStartTime())))
            .and(hasEndTime(LocalDateTime.from(jobSearchRequest.getEndTime())));

    // Lấy danh sách công việc theo các điều kiện tìm kiếm
    List<Job> jobs = jobRepository.findAll(specification);

    // Lọc bỏ các công việc mà seeker đã tương tác (LIKE hoặc DISLIKE)
    List<Job> filteredJobs =
        jobs.stream()
            .filter(
                job ->
                    seeker.getHistories().stream()
                        .noneMatch(history -> history.getJob().getId().equals(job.getId())))
            .collect(Collectors.toList());

    return filteredJobs;
  }

  private Specification<Job> hasCity(String city) {
    return (root, query, criteriaBuilder) ->
        criteriaBuilder.equal(root.join("location").get("city"), city);
  }

  private Specification<Job> hasStartTime(LocalDateTime startTime) {
    return (root, query, criteriaBuilder) ->
        criteriaBuilder.greaterThanOrEqualTo(root.join("workingHours").get("startTime"), startTime);
  }

  private Specification<Job> hasEndTime(LocalDateTime endTime) {
    return (root, query, criteriaBuilder) ->
        criteriaBuilder.lessThanOrEqualTo(root.join("workingHours").get("endTime"), endTime);
  }

  /**
   * Định nghĩa hàm tương tác với job
   *
   * @param jobId
   * @param interactionType
   */
  public ResponseObject interactWithJob(Long userId, Long jobId, InteractionType interactionType) {
    if (seekerRepository.findByUserId(userId).isEmpty()) {
      return ResponseObject.message("There Aren't any seeker with the requested id");
    }
    if (jobRepository.findById(jobId).isEmpty()) {
      return ResponseObject.message("There Aren't any job with the requested id");
    }
    Job job = jobRepository.findById(jobId).get();
    Seeker seeker = seekerRepository.findByUserId(userId).get();

    HistoryId historyId = new HistoryId(seeker, job);
    History history;

    if (historyRepository
        .findByPrimaryKeySeekerIdAndPrimaryKeyJobId(seeker.getId(), jobId)
        .isPresent()) {
      history =
          historyRepository.findByPrimaryKeySeekerIdAndPrimaryKeyJobId(seeker.getId(), jobId).get();
      history.setInteractionType(interactionType);
    } else {
      history = new History(historyId, interactionType);
    }
    historyRepository.save(history);
    return ResponseObject.ok();
  }

  public ResponseObject create(Long id, JobRequest request) {
    if (hiringOrganizationRepository.findByUserId(id).isEmpty()) {
      return ResponseObject.message("There Aren't any HR with the provided Id");
    }
    if (request.getLocationId() == null) {
      return ResponseObject.message("The Location Id must not be null");
    }
    if (locationRepository.findById(request.getLocationId()).isEmpty()) {
      return ResponseObject.message("There Aren't any Location with the provided Id");
    }

    HiringOrganization hiringOrganization = hiringOrganizationRepository.findByUserId(id).get();
    Location location = locationRepository.findById(request.getLocationId()).get();

    Image mainImage = null;
    List<Image> subImageList = new ArrayList<>();
    List<Image> galleryList = new ArrayList<>();

    // Main Image handling
    if (request.getMainImageUrl() != null) {
      mainImage = new Image(request.getMainImageUrl(), request.getMainImageDescription());
      mainImage = imageRepository.save(mainImage);
    }

    // Sub-Image handling
    if (request.getSubImageUrl() != null && request.getSubImageUrl().size() > 0) {
      IntStream.range(0, request.getSubImageUrl().size())
          .forEach(
              index -> {
                String description = null;
                try {
                  description = request.getSubImageDescriptions().get(index);
                } catch (Exception e) {
                  logger.error(
                      "Missing description for index number of subImage: "
                          + index
                          + " Error: "
                          + e);
                }
                Image image = new Image(request.getSubImageUrl().get(index), description);
                Image saved = imageRepository.save(image);
                subImageList.add(saved);
              });
    }

    //     Handle Photo Gallery (Not used)
    if (request.getGalleryUrl() != null && request.getGalleryUrl().size() > 0) {
      IntStream.range(0, request.getGalleryUrl().size())
          .forEach(
              index -> {
                String description = null;
                try {
                  description = request.getGalleryDescription().get(index);
                } catch (Exception e) {
                  logger.error(
                      "Missing description for index number of gallery: " + index + " Error: " + e);
                }
                Image image = new Image(request.getGalleryUrl().get(index), description);
                Image savedImage = imageRepository.save(image);
                galleryList.add(savedImage);
              });
    }
    PhotoGallery photoGallery = new PhotoGallery(galleryList, !galleryList.isEmpty());
    photoGallery = photoGalleryRepository.save(photoGallery);

    // Search label handling
    List<SearchLabel> labels = new ArrayList<>();
    request
        .getSearchLabelIds()
        .forEach(
            Id -> {
              if (searchLabelRepository.findById(Id).isPresent()) {
                labels.add(searchLabelRepository.findById(Id).get());
              }
            });

    // Web Application handling
    WebApplication webApplication = new WebApplication();
    if (!request.getWebApplicationUrl().isEmpty()) {
      webApplication.setUrl(request.getWebApplicationUrl());
      webApplication.setIs_available(true);
    } else {
      webApplication.setIs_available(false);
    }

    // Post Script Handling
    List<PostScript> postScripts = new ArrayList<>();
    IntStream.range(0, request.getPostScripts().size())
        .forEach(
            index -> {
              PostScript temp = new PostScript(request.getPostScripts().get(index), index);
              temp = postScriptRepository.save(temp);
              postScripts.add(temp);
            });

    // Working hour handling
    List<WorkingHour> workingHours = new ArrayList<>();
    request
        .getWorkingHours()
        .forEach(
            workingHourRequest -> {
              WorkingHour workingHour =
                  new WorkingHour(
                      workingHourRequest.getHours(),
                      workingHourRequest.getStart_time(),
                      workingHourRequest.getEnd_time(),
                      workingHourRequest.getIs_full_time());
              workingHour = workingHourRepository.save(workingHour);
              workingHours.add(workingHour);
            });

    // Handle Properties
    List<Property> properties = new ArrayList<>();
    request
        .getProperties()
        .forEach(
            propertyRequest -> {
              Property property =
                  new Property(
                      propertyRequest.getBody(),
                      propertyRequest.getTitle(),
                      propertyRequest.getSort_order(),
                      propertyRequest.getIs_displayed());
              property = propertyRepository.save(property);
              properties.add(property);
            });

    // Handle Company Survey
    CompanySurvey companySurvey = new CompanySurvey();
    companySurvey.setContents(request.getCompanySurvey().getContents());
    companySurvey.setDisplayed(request.getCompanySurvey().isDisplayed());

    // Handle Barometer
    Barometer barometer = new Barometer();
    List<String> barometerContents = new ArrayList<>();
    IntStream.range(0, request.getBarometer().getContents().size())
        .forEach(
            index -> {
              String temp =
                  request.getBarometer().getContents().get(index)
                      + request.getBarometer().getStats().get(index);
              barometerContents.add(temp);
            });
    barometer.setContents(barometerContents);
    barometer.setDisplayed(request.getBarometer().isDisplayed());

    // Handle Interview
    Interview interview = new Interview();
    interview.setContents(request.getInterview().getContents());
    interview.setDisplayed(request.getInterview().isDisplayed());

    // Handle Salary
    Salary salary = new Salary();
    salary.setDaily(request.getSalary().getDaily());
    salary.setHourly(request.getSalary().getHourly());
    salary.setMonthly(request.getSalary().getMonthly());
    salary.setDailyText(request.getSalary().getDailyText());
    salary.setHourlyText(request.getSalary().getHourlyText());
    salary.setMonthlyText(request.getSalary().getMonthlyText());
    salary.setType(request.getSalary().getType());
    salary = salaryRepository.save(salary);

    // Compiling together to one Job object
    Job job =
        new Job(
            hiringOrganization,
            request.getOpensAt(),
            request.getExpiresAt(),
            mainImage,
            request.getTitle(),
            request.getJobTitleCatchPhrase(),
            location,
            salary,
            workingHours,
            labels,
            postScripts,
            request.getCatchText(),
            request.getLeadText(),
            subImageList,
            properties,
            photoGallery,
            request.getProductCode());
    job.setWebApplication(webApplication);
    webApplication.setJob(job);
    job.setCompanySurvey(companySurvey);
    companySurvey.setJob(job);
    job.setBarometer(barometer);
    barometer.setJob(job);
    job.setInterview(interview);
    interview.setJob(job);
    jobRepository.save(job);

    return ResponseObject.ok();
  }

  public ResponseObject update(Long id, JobRequest object) {
    return null;
  }

  public ResponseObject delete(Long id) {
    return null;
  }
}
