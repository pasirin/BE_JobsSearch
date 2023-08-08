package com.example.JobsSearch.repository;

import com.example.JobsSearch.model.History;
import com.example.JobsSearch.model.util.InteractionType;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface HistoryRepository extends JpaRepository<History, Long> {
    Optional<History> findByPrimaryKeySeekerIdAndPrimaryKeyJobId(Long seekerId, Long jobId);
    // Lấy danh sách các History có interactionType là LIKE dựa trên jobId
    List<History> findByPrimaryKeyJobIdAndInteractionType(Long jobId, InteractionType interactionType);
}
