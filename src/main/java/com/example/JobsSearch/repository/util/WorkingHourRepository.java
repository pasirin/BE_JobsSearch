package com.example.JobsSearch.repository.util;

import com.example.JobsSearch.model.util.WorkingHour;
import org.springframework.data.jpa.repository.JpaRepository;

public interface WorkingHourRepository extends JpaRepository<WorkingHour, Long> {
}
