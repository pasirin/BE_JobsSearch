package com.example.JobsSearch.service.impl;

import com.example.JobsSearch.model.History;
import com.example.JobsSearch.model.Job;
import com.example.JobsSearch.model.Seeker;
import com.example.JobsSearch.model.util.HistoryId;
import com.example.JobsSearch.models.util.InteractionType;
import com.example.JobsSearch.payload.Request.JobRequest;
import com.example.JobsSearch.payload.Response.ResponseObject;
import com.example.JobsSearch.repository.HistoryRepository;
import com.example.JobsSearch.repository.JobRepository;
import com.example.JobsSearch.repository.SeekerRepository;
import com.example.JobsSearch.service.ServiceCRUD;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class JobService implements ServiceCRUD<JobRequest, Job> {
    @Autowired
    JobRepository jobRepository;

    @Autowired
    HistoryRepository historyRepository;

    @Autowired
    SeekerRepository seekerRepository;

    @Override
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

    @Override
    public Optional<Job> getById(Long jobId) {
        return jobRepository.findById(jobId);
    }


    /**
     * Định nghĩa hàm tìm kiếm cơ bản
     */
    public List<Job> searchJobs(String city, LocalDateTime startTime, LocalDateTime endTime) {
        Specification<Job> specification = Specification.where(hasCity(city))
                .and(hasStartTime(startTime))
                .and(hasEndTime(endTime));

        return jobRepository.findAll(specification);
    }

    public List<Job> searchJobs(Long seekerId, String city, LocalDateTime startTime, LocalDateTime endTime) {
        Seeker seeker = seekerRepository.findById(seekerId)
                .orElseThrow(() -> new RuntimeException("Seeker not found with id: " + seekerId));

        Specification<Job> specification = Specification.where(hasCity(city))
                .and(hasStartTime(startTime))
                .and(hasEndTime(endTime));

        // Lấy danh sách công việc theo các điều kiện tìm kiếm
        List<Job> jobs = jobRepository.findAll(specification);

        // Lọc bỏ các công việc mà seeker đã tương tác (LIKE hoặc DISLIKE)
        List<Job> filteredJobs = jobs.stream()
                .filter(job -> seeker.getHistories().stream()
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
        Seeker seeker = seekerRepository.findById(seekerId)
                .orElseThrow(() -> new RuntimeException("Seeker not found with id: " + seekerId));

        Job job = jobRepository.findById(jobId)
                .orElseThrow(() -> new RuntimeException("Job not found with id: " + jobId));

        // Kiểm tra xem seeker đã tương tác với job này chưa
        boolean isInteracted = seeker.getHistories().stream()
                .anyMatch(history -> history.getJob().getId().equals(jobId));

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

    @Override
    public ResponseObject create(JobRequest object) {
        return null;
    }

    @Override
    public ResponseObject update(Long id, JobRequest object) {
        return null;
    }

    @Override
    public ResponseObject delete(Long id) {
        return null;
    }

}
