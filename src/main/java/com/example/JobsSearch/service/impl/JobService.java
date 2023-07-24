package com.example.JobsSearch.service.impl;

import com.example.JobsSearch.model.HiringOrganization;
import com.example.JobsSearch.model.History;
import com.example.JobsSearch.model.Job;
import com.example.JobsSearch.model.Seeker;
import com.example.JobsSearch.model.util.*;
import com.example.JobsSearch.models.util.InteractionType;
import com.example.JobsSearch.payload.Request.JobRequest;
import com.example.JobsSearch.payload.Response.ResponseObject;
import com.example.JobsSearch.repository.*;
import com.example.JobsSearch.repository.util.*;
import com.example.JobsSearch.service.ServiceCRUD;
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

  @Autowired WebApplicationRepository webApplicationRepository;

  @Autowired PostScriptRepository postScriptRepository;

  @Autowired WorkingHourRepository workingHourRepository;

  @Autowired PropertyRepository propertyRepository;

  @Autowired CompanySurveyRepository companySurveyRepository;

  @Autowired BarometerRepository barometerRepository;

  @Autowired InterviewRepository interviewRepository;

  @Autowired SalaryRepository salaryRepository;

  public Collection<Job> getAll() {
    return jobRepository.findAll();
  }

  // lấy ra tất cả các job của organization
  public ResponseObject getAllJobsByOrganization(Long organizationId) {
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

  public Collection<Job> searchJobs(
      Long seekerId, String city, LocalDateTime startTime, LocalDateTime endTime) {
    Seeker seeker =
        seekerRepository
            .findById(seekerId)
            .orElseThrow(() -> new RuntimeException("Seeker not found with id: " + seekerId));

    Specification<Job> specification =
        Specification.where(hasCity(city)).and(hasStartTime(startTime)).and(hasEndTime(endTime));

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
   * @param seekerId
   * @param jobId
   * @param interactionType
   */
  public void interactWithJob(Long seekerId, Long jobId, InteractionType interactionType) {
    Seeker seeker =
        seekerRepository
            .findById(seekerId)
            .orElseThrow(() -> new RuntimeException("Seeker not found with id: " + seekerId));

    Job job =
        jobRepository
            .findById(jobId)
            .orElseThrow(() -> new RuntimeException("Job not found with id: " + jobId));

    // Kiểm tra xem seeker đã tương tác với job này chưa
    boolean isInteracted =
        seeker.getHistories().stream().anyMatch(history -> history.getJob().getId().equals(jobId));

    // Nếu seeker đã tương tác rồi thì không làm gì cả
    if (isInteracted) {
      return;
    }

    // Nếu là LIKE hoặc DISLIKE, lưu thông tin tương tác vào lịch sử
    if (interactionType == InteractionType.LIKE || interactionType == InteractionType.DISLIKE) {
      HistoryId historyId = new HistoryId(seeker, job);
      History history = new History(historyId, interactionType);
      historyRepository.save(history);

      // Cập nhật lại danh sách công việc hiển thị cho seeker
      seeker.getHistories().add(history);
      seekerRepository.save(seeker);

      // Cập nhật lại danh sách tương tác cho job
      job.getHistories().add(history);
      jobRepository.save(job);
    }
  }

  public ResponseObject create(
      Long id,
      JobRequest request,
      MultipartFile MainImage,
      List<MultipartFile> subImage,
      List<MultipartFile> gallery) {
    if (hiringOrganizationRepository.findByUserId(id).isEmpty()) {
      return ResponseObject.message("There Aren't any HR with the provided Id");
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
    if (MainImage != null) {
      try {
        String mainImageUrl = imageUploadService.uploadImage(MainImage);
        mainImage = new Image(mainImageUrl, request.getMainImageDescription());
        mainImage = imageRepository.save(mainImage);
      } catch (IOException e) {
        logger.error(String.valueOf(e));
      }
    }

    // Sub-Image handling
    if (subImage != null && subImage.size() > 0) {
      IntStream.range(0, subImage.size())
          .forEach(
              index -> {
                MultipartFile temp = subImage.get(index);
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
                try {
                  String subImageUrl = imageUploadService.uploadImage(temp);
                  Image image = new Image(subImageUrl, description);
                  Image saved = imageRepository.save(image);
                  subImageList.add(saved);
                } catch (IOException e) {
                  logger.error(String.valueOf(e));
                }
              });
    }

    //     Handle Photo Gallery (Not used)
    if (gallery != null && gallery.size() > 0) {
      IntStream.range(0, gallery.size())
          .forEach(
              index -> {
                MultipartFile temp = gallery.get(index);
                String description = null;
                try {
                  description = request.getGalleryDescription().get(index);
                } catch (Exception e) {
                  logger.error(
                      "Missing description for index number of gallery: " + index + " Error: " + e);
                }
                try {
                  String galleryImageUrl = imageUploadService.uploadImage(temp);
                  Image image = new Image(galleryImageUrl, description);
                  Image savedImage = imageRepository.save(image);
                  galleryList.add(savedImage);
                } catch (IOException e) {
                  logger.error(String.valueOf(e));
                }
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
