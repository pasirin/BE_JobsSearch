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

import java.time.LocalTime;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@Service
public class JobService {
    private static final Logger logger = LoggerFactory.getLogger(JobService.class);

    @Autowired
    JobRepository jobRepository;

    @Autowired
    HistoryRepository historyRepository;

    @Autowired
    SeekerRepository seekerRepository;

    @Autowired
    HiringOrganizationRepository hiringOrganizationRepository;

    @Autowired
    ImageRepository imageRepository;

    @Autowired
    LocationRepository locationRepository;

    @Autowired
    SearchLabelRepository searchLabelRepository;

    @Autowired
    PostScriptRepository postScriptRepository;

    @Autowired
    WorkingHourRepository workingHourRepository;

    @Autowired
    PropertyRepository propertyRepository;


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
    return ResponseObject.ok().setData(jobList);
  }

    public ResponseObject getJobByIdByOrganization(Long userId, Long jobId) {
        if (hiringOrganizationRepository.findByUserId(userId).isEmpty()) {
            return ResponseObject.message("There Aren't any HR with the requested Id");
        }
        if (jobRepository.findById(jobId).isEmpty()) {
            return ResponseObject.message("There Aren't any job with the requested Id");
        }
        HiringOrganization organization = hiringOrganizationRepository.findByUserId(userId).get();
        Job job = jobRepository.findById(jobId).get();
        if (!job.getOrganization().getId().equals(organization.getId())) {
            return ResponseObject.message("this job does not belong to this organization");
        }
        return ResponseObject.ok().setData(job);
    }

    public Optional<Job> getById(Long jobId) {
        return jobRepository.findById(jobId);
    }

    /**
     * Định nghĩa hàm tìm kiếm cơ bản
     */
    public ResponseObject searchJobs(Long userId, JobSearchRequest jobSearchRequest) {
        if (seekerRepository.findByUserId(userId).isEmpty()) {
            return ResponseObject.message("There Aren't any seeker with the requested id");
        }
        Seeker seeker = seekerRepository.findByUserId(userId).get();

        Specification<Job> specification = Specification.where(null);

        List<String> cityName = new ArrayList<>();

        if (jobSearchRequest.getLocation1() != null && !jobSearchRequest.getLocation1().isEmpty()) {
            cityName.add(jobSearchRequest.getLocation1());
        }
        if (jobSearchRequest.getStartTime() != null) {
            specification = specification.and(hasStartTime(jobSearchRequest.getStartTime()));
        }
        if (jobSearchRequest.getEndTime() != null) {
            specification = specification.and(hasEndTime(jobSearchRequest.getEndTime()));
        }
        if (jobSearchRequest.getAdvanceSearch() != null && jobSearchRequest.getAdvanceSearch()) {
            if (jobSearchRequest.getLocation2() != null && !jobSearchRequest.getLocation2().isEmpty()) {
                cityName.add(jobSearchRequest.getLocation2());
            }
            if (jobSearchRequest.getLocation3() != null && !jobSearchRequest.getLocation3().isEmpty()) {
                cityName.add(jobSearchRequest.getLocation3());
            }
            if (jobSearchRequest.getTagId() != null && !jobSearchRequest.getTagId().isEmpty()) {
                specification = specification.and(hasSearchTag(jobSearchRequest.getTagId()));
            }
        }

        if (!cityName.isEmpty()) {
            specification = specification.and(hasCity(cityName));
        }

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
        if (jobSearchRequest.getOnly_meta() != null && jobSearchRequest.getOnly_meta()) {
            return ResponseObject.ok().setData(filteredJobs.size());
        }

        return ResponseObject.ok().setData(filteredJobs);
    }

    private Specification<Job> hasCity(List<String> city) {
        return (root, query, criteriaBuilder) -> {
            if (city != null && !city.isEmpty()) {
                return root.join("location").get("city").in(city);
            } else {
                return criteriaBuilder.and();
            }
        };
    }

    private Specification<Job> hasStartTime(LocalTime startTime) {
        return (root, query, criteriaBuilder) ->
                criteriaBuilder.greaterThanOrEqualTo(root.join("workingHours").get("start"), startTime);
    }

    private Specification<Job> hasEndTime(LocalTime endTime) {
        return (root, query, criteriaBuilder) ->
                criteriaBuilder.lessThanOrEqualTo(root.join("workingHours").get("end"), endTime);
    }

    private Specification<Job> hasSearchTag(List<Long> tagId) {
        return (root, query, criteriaBuilder) -> {
            if (tagId != null && !tagId.isEmpty()) {
                return root.join("searchLabels").get("id").in(tagId);
            } else {
                return criteriaBuilder.and();
            }
        };
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

        // Handle Photo Gallery
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
        PhotoGallery photoGallery = new PhotoGallery(galleryList);

        // Search label handling
        List<SearchLabel> labels = new ArrayList<>();
        try {
            if (request.getSearchLabelIds() != null) {
                request
                        .getSearchLabelIds()
                        .forEach(
                                Id -> {
                                    if (searchLabelRepository.findById(Id).isPresent()) {
                                        labels.add(searchLabelRepository.findById(Id).get());
                                    } else {
                                        throw new RuntimeException();
                                    }
                                });
            }
        } catch (RuntimeException e) {
            return ResponseObject.message("The requested search Label doesn't exist");
        }

        // Web Application handling
        WebApplication webApplication = new WebApplication();
        if (!request.getWebApplicationUrl().isEmpty()) {
            webApplication.setUrl(request.getWebApplicationUrl());
            webApplication.setIsAvailable(true);
        } else {
            webApplication.setIsAvailable(false);
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

    public ResponseObject delete(Long userId, Long jobId) {
        if (hiringOrganizationRepository.findByUserId(userId).isEmpty()) {
            return ResponseObject.message("There Aren't any HR with the requested Id");
        }
        if (jobRepository.findById(jobId).isEmpty()) {
            return ResponseObject.message("There Aren't any job with the requested Id");
        }
        HiringOrganization organization = hiringOrganizationRepository.findByUserId(userId).get();
        Job job = jobRepository.findById(jobId).get();
        if (!job.getOrganization().getId().equals(organization.getId())) {
            return ResponseObject.message("this job does not belong to this organization");
        }
        // xóa dữ liệu của các bảng con
        //        imageRepository.deleteByJobId(jobId);
        //        salaryRepository.deleteByJobId(jobId);
        //        workingHourRepository.deleteByJobId(jobId);
        //
        //        imageRepository.deleteAllByJobId(jobId);
        // xóa dữ liệu bảng job
        jobRepository.deleteById(jobId);
        return ResponseObject.ok();
    }

    public ResponseObject update(Long id, Long userId, JobRequest jobRequest) {
        if (hiringOrganizationRepository.findByUserId(userId).isEmpty()) {
            return ResponseObject.message("There Aren't any HR with the requested Id");
        }
        if (jobRepository.findById(id).isEmpty()) {
            return ResponseObject.message("There Aren't any job with the requested Id");
        }
        HiringOrganization hiringOrganization = hiringOrganizationRepository.findByUserId(userId).get();
        Job job = jobRepository.findById(id).get();
        if (!job.getHRId().equals(hiringOrganization.getId())) {
            return ResponseObject.message("You Do Not Have Authority To Update This Job");
        }
        job.setOpensAt(jobRequest.getOpensAt());
        job.setExpiresAt(jobRequest.getExpiresAt());

        // Main image
        Image image = job.getMainImage();
        Image newMainImage = new Image();
        if (jobRequest.getMainImageUrl() == null
                || !image.getUrl().equals(jobRequest.getMainImageUrl())) {
            newMainImage.setUrl(jobRequest.getMainImageUrl());
        }
        if (jobRequest.getMainImageDescription() != null) {
            newMainImage.setDescription(jobRequest.getMainImageDescription());
        }
        job.setMainImage(newMainImage);

        job.setTitle(jobRequest.getTitle());
        job.setJobTitleCatchPhrase(jobRequest.getJobTitleCatchPhrase());

        // Location
        if (locationRepository.findById(jobRequest.getLocationId()).isEmpty()) {
            return ResponseObject.message("There Aren't any location with the requested Id");
        }
        Location newLocation = locationRepository.findById(jobRequest.getLocationId()).get();
        job.setLocation(newLocation);

        // Salary
        Salary newSalary = new Salary();
        newSalary.setDaily(jobRequest.getSalary().getDaily());
        newSalary.setHourly(jobRequest.getSalary().getHourly());
        newSalary.setMonthly(jobRequest.getSalary().getMonthly());
        newSalary.setDailyText(jobRequest.getSalary().getDailyText());
        newSalary.setHourlyText(jobRequest.getSalary().getHourlyText());
        newSalary.setMonthlyText(jobRequest.getSalary().getMonthlyText());
        newSalary.setType(jobRequest.getSalary().getType());

        job.setSalary(newSalary);

        // Working Hours
        List<WorkingHour> oldWorkingHours = job.getWorkingHours();
        oldWorkingHours.clear();
        jobRequest
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
                            oldWorkingHours.add(workingHour);
                        });

        // Search Label
        List<SearchLabel> newLabels = new ArrayList<>();
        try {
            if (jobRequest.getSearchLabelIds() != null) {
                jobRequest
                        .getSearchLabelIds()
                        .forEach(
                                Id -> {
                                    if (searchLabelRepository.findById(Id).isPresent()) {
                                        newLabels.add(searchLabelRepository.findById(Id).get());
                                    } else {
                                        throw new RuntimeException();
                                    }
                                });
            }
        } catch (RuntimeException e) {
            return ResponseObject.message("The requested search Label doesn't exist");
        }

        job.setSearchLabels(newLabels);

        // Web Application
        WebApplication oldWebApplication = job.getWebApplication();
        if (!jobRequest.getWebApplicationUrl().isEmpty()) {
            oldWebApplication.setUrl(jobRequest.getWebApplicationUrl());
            oldWebApplication.setIsAvailable(true);
        } else {
            oldWebApplication.setIsAvailable(false);
        }

        // Post Script
        List<PostScript> oldPostScripts = job.getPostScripts();
        oldPostScripts.clear();
        IntStream.range(0, jobRequest.getPostScripts().size())
                .forEach(
                        index -> {
                            PostScript temp = new PostScript(jobRequest.getPostScripts().get(index), index);
                            temp = postScriptRepository.save(temp);
                            oldPostScripts.add(temp);
                        });


        job.setCatchText(jobRequest.getCatchText());
        job.setLeadText(jobRequest.getLeadText());

        // Sub Images
        List<Image> newSubImageList = new ArrayList<>();
        if (jobRequest.getSubImageUrl() != null && jobRequest.getSubImageUrl().size() > 0) {
            IntStream.range(0, jobRequest.getSubImageUrl().size())
                    .forEach(
                            index -> {
                                String description = null;
                                try {
                                    description = jobRequest.getSubImageDescriptions().get(index);
                                } catch (Exception e) {
                                    logger.error(
                                            "Missing description for index number of subImage: "
                                                    + index
                                                    + " Error: "
                                                    + e);
                                }
                                Image tempImage = new Image(jobRequest.getSubImageUrl().get(index), description);
                                Image saved = imageRepository.save(tempImage);
                                newSubImageList.add(saved);
                            });
        }
        job.setSubImages(newSubImageList);

        // Property
        List<Property> oldProperties = job.getProperties();
        oldProperties.clear();
        jobRequest
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
                            oldProperties.add(property);
                        });

        // Company Survey
        CompanySurvey oldCompanySurvey = job.getCompanySurvey();
        oldCompanySurvey.setContents(jobRequest.getCompanySurvey().getContents());
        oldCompanySurvey.setDisplayed(jobRequest.getCompanySurvey().isDisplayed());

        // Barometer
        Barometer oldBarometer = job.getBarometer();
        List<String> barometerContents = new ArrayList<>();
        IntStream.range(0, jobRequest.getBarometer().getContents().size())
                .forEach(
                        index -> {
                            String temp =
                                    jobRequest.getBarometer().getContents().get(index)
                                            + jobRequest.getBarometer().getStats().get(index);
                            barometerContents.add(temp);
                        });
        oldBarometer.setContents(barometerContents);
        oldBarometer.setDisplayed(jobRequest.getBarometer().isDisplayed());

        // Gallery
        List<Image> newGalleryList = new ArrayList<>();
        if (jobRequest.getGalleryUrl() != null && jobRequest.getGalleryUrl().size() > 0) {
            IntStream.range(0, jobRequest.getGalleryUrl().size())
                    .forEach(
                            index -> {
                                String description = null;
                                try {
                                    description = jobRequest.getGalleryDescription().get(index);
                                } catch (Exception e) {
                                    logger.error(
                                            "Missing description for index number of gallery: " + index + " Error: " + e);
                                }
                                Image tempImage = new Image(jobRequest.getGalleryUrl().get(index), description);
                                Image savedImage = imageRepository.save(tempImage);
                                newGalleryList.add(savedImage);
                            });
        }
        PhotoGallery newPhotoGallery = new PhotoGallery(newGalleryList);
        job.setPhotoGallery(newPhotoGallery);

        // Interview
        Interview oldInterview = job.getInterview();
        oldInterview.setContents(jobRequest.getInterview().getContents());
        oldInterview.setDisplayed(jobRequest.getInterview().isDisplayed());

        job.setProductCode(jobRequest.getProductCode());

        jobRepository.save(job);
        return ResponseObject.ok();
    }

    public ResponseObject getLikedSeekerHistoriesByJobId(Long userId, Long jobId) {
        getJobByIdByOrganization(userId, jobId);
        List<History> likedHistories = historyRepository.findByPrimaryKeyJobIdAndInteractionType(jobId, InteractionType.LIKE);
        List<Seeker> likedSeekers = likedHistories.stream()
                .map(History::getSeeker)
                .collect(Collectors.toList());
        return ResponseObject.ok().setData(likedSeekers);
    }
}
