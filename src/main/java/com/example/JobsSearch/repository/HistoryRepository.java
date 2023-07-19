package com.example.JobsSearch.repository;

import com.example.JobsSearch.model.History;
import org.springframework.data.jpa.repository.JpaRepository;

public interface HistoryRepository extends JpaRepository<History, Long> {
}
