package com.example.JobsSearch.repository.util;

import com.example.JobsSearch.model.util.Interview;
import org.springframework.data.jpa.repository.JpaRepository;

public interface InterviewRepository extends JpaRepository<Interview, Long> {
}
