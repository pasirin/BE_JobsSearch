package com.example.JobsSearch.repository;

import com.example.JobsSearch.model.Job;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import javax.transaction.Transactional;
import java.util.List;

public interface JobRepository extends JpaRepository<Job, Long>, JpaSpecificationExecutor<Job> {
    List<Job> findByOrganizationId(Long organizationId);

    @Transactional
    void deleteAllByOrganizationId(Long id);
}
