package com.example.JobsSearch.repository.util;

import com.example.JobsSearch.model.util.Salary;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SalaryRepository extends JpaRepository<Salary, Long> {
}
