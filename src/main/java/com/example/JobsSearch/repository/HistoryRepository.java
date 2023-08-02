package com.example.JobsSearch.repository;

import com.example.JobsSearch.model.History;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface HistoryRepository extends JpaRepository<History, Long> {
    Optional<History> findByPrimaryKeySeekerIdAndPrimaryKeyJobId(Long seekerId, Long jobId);
}
