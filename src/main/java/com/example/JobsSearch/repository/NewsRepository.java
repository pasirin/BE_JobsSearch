package com.example.JobsSearch.repository;

import com.example.JobsSearch.model.News;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface NewsRepository extends JpaRepository<News, Long> {
    List<News> findByOrderByUpdatedAtDesc();
}
